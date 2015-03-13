(ns mars-rover.commands)

(def commands-by-message
  {"r" :rotate-right
   "l" :rotate-left
   "f" :move-forwards
   "b" :move-backwards})

(defn create-from [messages]
  (map #(commands-by-message (str %)) messages))