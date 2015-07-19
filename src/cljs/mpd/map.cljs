(ns mpd.client.map)

(def container
  (js/PIXI.Container.))

(def text
  (js/PIXI.Text. "hello"
                 {:fill "red"
                  :anchor {:x 0.5 :y 0.5}}))

(.addChild container text)
