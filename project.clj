(defproject mpd "0.1.0-SNAPSHOT"
  :description "Multiplayer deathmatch"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :source-paths ["src/clj" "src/cljs"]

  :dependencies [[org.clojure/clojure "1.7.0"]
                 [org.clojure/clojurescript "0.0-3308"]
                 [ring/ring-devel "1.3.2"]
                 [ring/ring-core "1.3.2"]
                 [ring.middleware.logger "0.5.0"]
                 [http-kit "2.1.18"]]

  :plugins      [[lein-cljsbuild "1.0.6"]
                 [lein-pdo "0.1.1"]
                 [lein-shell "0.4.1"]]
  :aliases {"up" ["pdo" "cljsbuild" "auto,"
                  "run" "-dev,"]}
  :cljsbuild {:builds
              [{:source-paths ["src/cljs"]
                :compiler {:output-to "resources/public/js/build/mpd.js"
                           :output-dir "resources/public/js/build"
                           :optimizations :none
                           :pretty-print true
                           :source-map true}}]}
  :main mpd.handler
  :aot [mpd.handler])
