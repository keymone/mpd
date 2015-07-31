(ns mpd.main
  (:require [mpd.shared :refer [log]]
            [mpd.stage :as stage]
            [mpd.player :as player]
            [mpd.collisions :as collisions]
            [mpd.network :as network]
            [mpd.game :as game]
            [mpd.bullets :as bullets]
            [mpd.enemies :as enemies]
            [mpd.assets :as assets]))

; Game: wires everything together, enforces game logic and rules
; Stage: rendering system, provides drawable entities
; Player: receive keyboard and mouse input, update player entity
; Network: send/receive events from server, translate them for Game

(log "Initializing all subsystems...")
(.play assets/music)
(game/setup
  (stage/setup)
  (player/setup)
  (network/setup)
  (bullets/setup)
  (collisions/setup)
  (enemies/setup))
