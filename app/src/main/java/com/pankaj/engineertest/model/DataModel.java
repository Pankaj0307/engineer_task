package com.pankaj.engineertest.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DataModel {
    @SerializedName("hits")
    private List<HitList> hits;

    public List<HitList> getHits() {
        return hits;
    }

    public void setHits(List<HitList> hits) {
        this.hits = hits;
    }

    public class HitList {

        private boolean count;

        public boolean isCount() {
            return count;
        }

        public void setCount(boolean count) {
            this.count = count;
        }

        @SerializedName("created_at")
        private String created_at;

        @SerializedName("title")
        private String title;

        @SerializedName("objectID")
        private String objectID;


        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getObjectID() {
            return objectID;
        }

        public void setObjectID(String objectID) {
            this.objectID = objectID;
        }
    }


}
