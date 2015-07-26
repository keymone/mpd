(ns mpd.player
  (:require [mpd.shared :refer [log]]))

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
              "default" nil)
        updown (case (.-type event)
                 "keydown" true
                 "keyup" false
                 "default" nil)]
    (when (not (or (= key nil) (= updown nil)))
      (when (not (.-repeat event))
        (swap! input assoc key updown)))))

(defn mousehandler [event]
  )

(defn movehandler [event]
  )

(defn start-listening []
  (aset js/document "onkeydown"   keyhandler)
  (aset js/document "onkeyup"     keyhandler)
  (aset js/document "onmousedown" mousehandler)
  (aset js/document "onmouseup"   mousehandler)
  (aset js/document "onmousemove" movehandler))

(defn setup [stage player network]
  (log "  player")
  (fn [state]
    (start-listening)
    (let [player (:player @state)
          x (:x player)
          y (:y player)
          speed (:speed player)
          dx (if (:left @input) -1 (if (:right @input) 1 0))
          dy (if (:up @input) -1 (if (:down @input) 1 0))
          ; moving diagonally should mean moving faster distance-wise
          nc (if (and (not (= dx 0)) (not (= dy 0))) 0.7071 1)]
      (when (not (= dy 0)) (swap! state assoc-in [:player :y] (+ y (* speed nc dy))))
      (when (not (= dx 0)) (swap! state assoc-in [:player :x] (+ x (* speed nc dx)))))
    state))
