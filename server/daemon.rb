require 'em-websocket'
require 'json'

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

    timer = EM.add_periodic_timer(0) {
      if inbox.any?
        inbox_tmp = inbox.clone
        inbox = []
        puts "New stuff in a queue, sending..."
        channel.each do |id, connection|
          inbox_tmp.each do |object|
            next if object['id'] == id.to_i
            connection.send object.to_json
            puts "Sent data from player #{object['id']} to player #{id}"
          end
        end
      end
    }

    network = Thread.new do
      ws.onopen {
        player_counter += 1
        player_id = player_counter
        puts "Player #{player_id} connected!"

        channel[player_id.to_s] = ws
        ws.send ({:id => player_id}.to_json)

        ws.onmessage { |msg|
          msg_hash = JSON.parse(msg) rescue {}
          inbox << msg_hash
          puts "Received from player #{msg_hash['id']}:\n#{msg_hash}"
        }

        ws.onclose {
          puts "Player #{player_id} disconnected!"
          channel.delete(player_id.to_s)
        }
      }
    end

  end
  puts "Multi[P]layer Deatchmatch Server started!"
  $stdout.flush
}

# 10.247.110.131:8080
