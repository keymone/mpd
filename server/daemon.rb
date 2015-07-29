require 'em-websocket'
require 'json'

tick = 0
frequency = 30

inbox = []
mutex = Mutex.new

ticker = Thread.new do
  loop do
    sleep(1.0/frequency) # 1/30s, wait ~ 2 frames
    mutex.synchronize do
      if inbox.any?
        puts "Processing current inbox queue having #{inbox.size} elements at tick #{tick}"
        inbox = [] # reset inbox queue
      end
    end
    tick += 60.0 / frequency # 2
  end
end

EventMachine.run {
  @channel = EM::Channel.new
  EventMachine::WebSocket.start(:host => "0.0.0.0", :port => 8192, :debug => true) do |ws|

    network = Thread.new do

      ws.onopen {
        sid = @channel.subscribe { |msg| ws.send msg }
        # @channel.push "#{sid} connected!"

        ws.onmessage { |msg|
          msg_hash = JSON.parse(msg) rescue {}

          mutex.synchronize do
            inbox << msg_hash
          end

          # print "Parsed message:\n#{msg_hash}\n"
          # @channel.push "<#{sid}>: #{msg}"
        }

        ws.onclose {
          @channel.unsubscribe(sid)
        }
      }

    end

  end
  puts "Multi[P]layer Deatchmatch Server started!"
}
