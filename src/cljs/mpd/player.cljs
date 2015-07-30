(ns mpd.player
  (:require [mpd.shared :refer [log]]
            [mpd.bullets :as bullets]
            [mpd.network :as network]))

; INPUTS

(def input (atom {:up false ; 38 87
                  :down false; 40 83
                  :left false; 37 65
                  :right false; 39 68
                  :click false
                  :rclick false
                  :mousex 0
                  :mousey 0}))

(defn keyhandler [event]
  (let [key (case (.-keyCode event)
              (38 87) :up
              (40 83) :down
              (37 65) :left
              (39 68) :right
              nil)
        updown (case (.-type event)
                 "keydown" true
                 "keyup" false
                 nil)]
    (when (not (or (= key nil) (= updown nil)))
      (when (not (.-repeat event))
        (swap! input assoc key updown)))))

(defn mousehandler [event]
  (let [key (case (.-button event) 0 :click 2 :rclick nil)
        val (case (.-type event) "mousedown" true "mouseup" false nil)]
    (when (not (and (= nil key) (= nil val)))
      (swap! input assoc key val))))

(defn movehandler [event]
  (swap! input assoc :mousex (.-clientX event))
  (swap! input assoc :mousey (.-clientY event)))

(defn start-listening []
  (aset js/document "onkeydown"   keyhandler)
  (aset js/document "onkeyup"     keyhandler)
  (aset js/document "onmousedown" mousehandler)
  (aset js/document "onmouseup"   mousehandler)
  (aset js/document "onmousemove" movehandler))

; FIRE

(defn make-bullet [player]
  {:x (+ (:x player) (* (Math/cos (+ 0.17 (:rotation player))) 80))
   :y (+ (:y player) (* (Math/sin (+ 0.17 (:rotation player))) 80))
   :angle (:rotation player)
   :speed 10
   :distance 100
   :damage 100
   :type "whatever"
   :delay 500})

(def fire_timer (atom nil))
; add to assets on game load and remove it from here
(def fire_sound (js/buzz.sound. "sound/fire.wav" (js-obj :formats ["wav"])))

(defn fire [bullet]
  (when (nil? @fire_timer)
    (reset! fire_timer (:delay bullet))
    (js/setTimeout #(reset! fire_timer nil) (:delay bullet))
    (network/attach bullet)
    (bullets/fire bullet)))
; SETUP

(defn setup []
  (log "  player")
  (start-listening)
  (fn [state]
    (let [player (:player @state)
          x (:x player) y (:y player)
          ; movement
          speed (:speed player)
          dx (if (:left @input) -1 (if (:right @input) 1 0))
          dy (if (:up @input) -1 (if (:down @input) 1 0))
          ; moving diagonally should mean moving faster distance-wise
          nc (if (and (not (= dx 0)) (not (= dy 0))) 0.7071 1)
          ; rotation
          mx (:mousex @input) my (:mousey @input)]
      (when (not (= dy 0)) (swap! state assoc-in [:player :y] (+ y (* speed nc dy))))
      (when (not (= dx 0)) (swap! state assoc-in [:player :x] (+ x (* speed nc dx))))
      (swap! state assoc-in [:player :rotation] (Math/atan2 (- my y) (- mx x)))
      (swap! state assoc-in [:player :primary] (:click @input))
      (swap! state assoc-in [:player :secondary] (:rclick @input))
      (swap! state assoc-in [:crosshair :x] (:mousex @input))
      (swap! state assoc-in [:crosshair :y] (:mousey @input))
      (when (:click @input) (fire (make-bullet player))))
    state))
