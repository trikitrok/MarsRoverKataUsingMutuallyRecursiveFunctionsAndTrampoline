(ns mars-rover.core
  (:require [mars-rover.commands :as commands]
            [mars-rover.worlds :refer [infinite-world hit-obstacle?]]
            [mars-rover.rover :as rover]))

(defn- validate-initial-position [rover {obstacles :obstacles}]
  (when (hit-obstacle? rover obstacles)
    (throw (IllegalArgumentException. 
             "Initial position is on an obstacle!"))))

(defn receive [rover messages & {world :world :or {world infinite-world}}]
  (validate-initial-position rover world)
  (rover/process (commands/create-from messages) rover world))