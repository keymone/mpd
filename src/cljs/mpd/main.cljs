(ns mpd.main
  (:require [mpd.shared :refer [log]]
            [mpd.stage :as stage]
            [mpd.player :as player]
            [mpd.enemies :as enemies]
            [mpd.network :as network]
            [mpd.game :as game]
            [mpd.bullets :as bullets]))

; Game: wires everything together, enforces game logic and rules
; Stage: rendering system, provides drawable entities
; Player: receive keyboard and mouse input, update player entity
; Network: send/receive events from server, translate them for Game

(log "Initializing all subsystems...")
(game/setup
  (stage/setup)
  (player/setup)
  (network/setup)
  (bullets/setup)
  (enemies/setup))
