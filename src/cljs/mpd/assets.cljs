(ns mpd.assets
  (:require [mpd.shared :refer [log]]))

(defn puke_sprite [imagePath scale randomTint]
  (let [texture (js/PIXI.Texture.fromImage imagePath)
        sprite (js/PIXI.Sprite. texture)]
    (aset sprite "anchor" (js-obj "x" 0.5 "y" 0.5))
    (aset sprite "scale" (js-obj "x" scale "y" scale))
    (when randomTint (aset sprite "tint" (* (Math/random) 16777215)))
    sprite))

(defn puke_sprite_for_texture [texture scale randomTint]
  (let [sprite (js/PIXI.Sprite. texture)]
    (aset sprite "anchor" (js-obj "x" 0.5 "y" 0.5))
    (aset sprite "scale" (js-obj "x" scale "y" scale))
    (when randomTint (aset sprite "tint" (* (Math/random) 16777215)))
    sprite))

(def health_bar
  (js/PIXI.Text. "0" #js {:fill "white" :font "bold 20px Arial"}))

(def score_status
  (js/PIXI.Text. "0" #js {:fill "white" :font "bold 50px Arial"}))

(def player_texture
  (js/PIXI.Texture.fromImage "images/player.png"))

(def arrow_texture
  (js/PIXI.Texture.fromImage "images/arrow.png"))

(defn arrow_sprite []
  (puke_sprite_for_texture arrow_texture 0.2 false))

(def bullet_texture
  (js/PIXI.Texture.fromImage "images/bullet.png"))

(def blood_texture
  (js/PIXI.Texture.fromImage "images/blood.png"))

(defn bullet_sprite []
  (puke_sprite_for_texture bullet_texture 1.0 false))

(def crosshair_sprite
  (puke_sprite "images/crosshair.png" 1.0 false))

(def background_tiling_sprite
  (let [bg (PIXI.extras.TilingSprite.fromImage "images/floor.png" 5000 5000)]
    ;(aset bg "anchor" (js-obj "x" 0.5 "y" 0.5))
    bg))

;sounds
(def fire_sound
  (js/buzz.sound. "sound/fire.wav" (js-obj :formats ["wav"])))

(def music
  (js/buzz.sound. "sound/music.mp3" (js-obj :formats ["mp3"])))
