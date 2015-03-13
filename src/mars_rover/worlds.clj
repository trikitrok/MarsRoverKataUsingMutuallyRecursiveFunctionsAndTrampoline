(ns mars-rover.worlds)

(defn square-world [x y size & obstacles]
  {:wrap-fn 
   (fn [{x-rover :x y-rover :y :as rover}] 
     (cond 
       (and (> y-rover y) 
            (> (- y-rover y) size)) 
       (assoc-in rover [:y] (- y-rover size))
       
       (and (< y-rover y) 
            (< (- y y-rover) size)) 
       (assoc-in rover [:y] (+ y-rover size))
       
       (and (> x-rover x) 
            (> (- x-rover x) size)) 
       (assoc-in rover [:x] (- x-rover size))
       
       (and (< x-rover x) 
            (< (- x x-rover) size)) 
       (assoc-in rover [:x] (+ x-rover size))
       
       :else rover))
   
   :obstacles obstacles})

(def infinite-world 
  {:wrap-fn identity
   
   :obstacles []})

(defn hit-obstacle? [{x-rover :x y-rover :y} obstacles]
  (= (some #{{:x x-rover :y y-rover}} obstacles) 
     {:x x-rover :y y-rover}))

(defn wrap [world rover]
  ((:wrap-fn world) rover))

(defn rover-not-hitting-obstacle [current-rover next-rover {obstacles :obstacles}]
  (if (hit-obstacle? next-rover obstacles)
    current-rover
    next-rover))