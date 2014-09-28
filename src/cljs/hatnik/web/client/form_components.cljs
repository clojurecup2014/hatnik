(ns hatnik.web.client.form-components
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [hatnik.web.client.z-actions :as action]))

(defn project-adding-from [data]
  (dom/div 
   #js {:className "modal-content"}
   (dom/div #js {:className "modal-header"}
            (dom/h4 #js {:className "modal-title"}
                    "Add project"))))


;; For selecting action form header

(def action-forms-headers 
  {:email-action "Add email notification"
   :email-edit-action "Edit email notification"})

(defn action-form-header [data owner]
  (reify
    om/IRender
    (render [this]
      (dom/h4 #js {:className "modal-title"}
              (get action-forms-headers
                   (:form-type data))))))


;; For selecting action form body

(defn email-action-form-body [data]
  (dom/form 
   #js {:id "email-action-form"}
   (dom/div #js {:className "form-group"}
            (dom/label #js {:for "artifact-input"} "Artifact")
            (dom/input #js {:type "text"
                            :className "form-control"
                            :id "artifact-input"
                            :placeholder "Artifact name"}))
   (dom/div #js {:className "form-group"}
            (dom/label #js {:for "emain-input"} "Email")
            (dom/input #js {:type "email"
                            :className "form-control"
                            :id "emain-input"
                            :value (:email (:user data))
                            :disabled "disabled"})
            (dom/div #js {:className "form-group"}
                     (dom/label #js {:for "emain-body-input"} "Email body")
                     (dom/textarea #js {:cols "40"
                                        :className "form-control"
                                        :id "emain-body-input"}
                                   "Library release: {{LIBRARY}} {{VERSION}}")))))

(defn email-edit-action [data]  
  (email-action-form-body data))

(def action-form-bodys 
  {:email-action email-action-form-body
   :email-edit-action email-edit-action})

(defn action-form-body [data body]
  (reify
    om/IRender
    (render [this]
      (apply 
       (get action-form-bodys (:form-type data))
       [data]))))

;; For selecting footer

(defn email-action-footer [data]
  (dom/div nil
           (dom/button #js {:className "btn btn-primary pull-left"
                            :onClick #(action/send-new-email-action (:current-project @data))} "Submit")
           (dom/button #js {:className "btn btn-default"
                            :onClick #(action/test-new-email-action (:current-project @data))} "Test")))

(defn email-edit-footer [data]
  (dom/div 
   nil
   (dom/button #js {:className "btn btn-primary pull-left"
                    :onClick #(action/update-email-action 
                               (:current-project @data)
                               (get (deref (:current-action @data)) "id"))} "Update")
   (dom/button #js {:className "btn btn-danger pull-center"
                    :onClick #(action/delete-action 
                               (get (deref (:current-action @data)) "id"))} "Delete")
   (dom/button #js {:className "btn btn-default"
                    :onClick #(action/test-new-email-action (:current-project @data))} "Test")))

(def action-form-footers
  {:email-action email-action-footer
   :email-edit-action email-edit-footer})

(defn action-form-footer [data body]
  (reify
    om/IRender
    (render [this]
      (apply 
       (get action-form-footers (:form-type data))
       [data]))))
