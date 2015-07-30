require 'em-websocket'
require 'json'

def pprint message
  puts message
  $stdout.flush
end

tick = 0
frequency = 30
inbox = []
player_counter = 0
channel = {}

ticker = Thread.new do
  loop do
    sleep(1.0/frequency) # 1/30s, wait ~ 2 frames
    tick += 1
  end
end

EventMachine.run {
  EventMachine::WebSocket.start(:host => "0.0.0.0", :port => 8197) do |ws|

    timer = EM.add_periodic_timer(1.0/60) {
      if inbox.any?
        inbox_tmp = inbox.clone.reverse
        inbox = []
        channel.each do |id, connection|
          inbox_tmp.each do |object|
            next if object['id'] == id.to_i
            connection.send object.to_json
            pprint "Sent data from player #{object['id']} to player #{id}"
          end
        end
      end
    }

    network = Thread.new do
      ws.onopen {
        sleep 1
        player_counter += 1
        player_id = player_counter
        pprint "Player #{player_id} connected!"

        channel[player_id.to_s] = ws
        ws.send ({:id => player_id}.to_json)

        ws.onmessage { |msg|
          msg_hash = JSON.parse(msg) rescue {}
          inbox << msg_hash
          pprint "Received from player #{msg_hash['id']}:\n#{msg_hash}"
        }

        ws.onclose {
          pprint "Player #{player_id} disconnected!"
          channel.delete(player_id.to_s)
        }
      }
    end

  end
  pprint "Multi[P]layer Deatchmatch Server started!"
}

# 10.247.110.131:8080
