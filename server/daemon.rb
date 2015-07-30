require 'em-websocket'
require 'json'

tick = 0
frequency = 30
inbox = []
player_id = 0

ticker = Thread.new do
  loop do
    sleep(1.0/frequency) # 1/30s, wait ~ 2 frames
    tick += 1
  end
end

EventMachine.run {
  @channel = EM::Channel.new
  EventMachine::WebSocket.start(:host => "0.0.0.0", :port => 8197, :debug => true) do |ws|

    timer = EM.add_periodic_timer(0) {
      if inbox.any?
        inbox_tmp = inbox.clone
        inbox = []
        inbox_tmp.each do |object|
          @channel.push object.to_json
        end
      end
    }

    network = Thread.new do
      ws.onopen {
        sid = @channel.subscribe { |msg| ws.send msg }
        player_id += 1
        ws.send ({:id => player_id}.to_json)

        ws.onmessage { |msg|
          msg_hash = JSON.parse(msg) rescue {}

          inbox << msg_hash

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
  $stdout.flush
}
