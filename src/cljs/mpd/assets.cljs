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
  (js/PIXI.Text. "0" #js {:fill "red"}))

(def player_texture
  (js/PIXI.Texture.fromImage "images/player.png"))

(def bullet_texture
  (js/PIXI.Texture.fromImage "images/bullet.png"))

(defn bullet_sprite []
  (puke_sprite_for_texture bullet_texture 1.0 false))

(def crosshair_sprite
  (puke_sprite "images/crosshair.png" 1.0 false))

(def background_tiling_sprite
  (let [bg (PIXI.extras.TilingSprite.fromImage "images/floor.png" 10000 10000)]
    (aset bg "scale" (js-obj "x" 0.5 "y" 0.5))
    (aset bg "anchor" (js-obj "x" 0.5 "y" 0.5))
    bg))

;sounds
(def fire_sound
  (js/buzz.sound. "sound/fire.wav" (js-obj :formats ["wav"])))
