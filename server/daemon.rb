require 'em-websocket'
require 'json'

EventMachine.run {
  @channel = EM::Channel.new

  EventMachine::WebSocket.start(:host => "0.0.0.0", :port => 8192, :debug => true) do |ws|

    ws.onopen {
      sid = @channel.subscribe { |msg| ws.send msg }
      # @channel.push "#{sid} connected!"

      ws.onmessage { |msg|
        msg_hash = JSON.parse(msg) rescue {}
        print "Parsed message:\n#{msg_hash}\n"
        # @channel.push "<#{sid}>: #{msg}"
      }

      ws.onclose {
        @channel.unsubscribe(sid)
      }
    }

  end

  puts "MPD server started"
}
