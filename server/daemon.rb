require 'em-websocket'
require 'json'

def pprint message
  puts message
  $stdout.flush
end

tick = 0
frequency = 30
ticker = Thread.new do
  loop do
    sleep(1.0/frequency) # 1/30s, wait ~ 2 frames
    tick += 1
  end
end

EventMachine.run do
  channel = {}
  players = {}
  player_counter = 0

  inbox = EM::Queue.new
  popper = Proc.new do |msg|
    channel.each {|id,conn| conn.send(msg.to_json) if msg['id'] != id }
    inbox.pop &popper
  end
  inbox.pop &popper

  EventMachine::WebSocket.start(:host => "0.0.0.0", :port => 8197) do |ws|
    ws.onopen do
      sleep 0.5
      player_counter += 1
      player_id = player_counter
      pprint "Player #{player_id} connected!"

      channel[player_id] = ws
      ws.send({:id => player_id}.to_json)
      players.each { |k,p| inbox.push(p) }

      ws.onmessage do |msg|
        msg_hash = JSON.parse(msg) rescue {}
        players[msg_hash['id']] = msg_hash
        inbox.push(msg_hash)
      end

      ws.onclose do
        pprint "Player #{player_id} disconnected!"
        channel.delete(player_id)
        players.delete(player_id)
        inbox.push({:type => 'remove', :id => player_id})
      end
    end
  end

  pprint "Multi[P]layer Deatchmatch Server started!"
end

# 10.247.110.131:8080
