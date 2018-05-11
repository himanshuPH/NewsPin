package com.news.android.himanshu.newspin.elasticView

interface ElasticDragDismissCallback {

    /**
     * Called for each drag event.
     *
     * @param elasticOffset       Indicating the drag offset with elasticity applied i.e. may
     * exceed 1.
     * @param elasticOffsetPixels The elastically scaled drag distance in pixels.
     * @param rawOffset           Value from [0, 1] indicating the raw drag offset i.e.
     * without elasticity applied. A value of 1 indicates that the
     * dismiss distance has been reached.
     * @param rawOffsetPixels     The raw distance the user has dragged
     */
    fun onDrag(elasticOffset: Float, elasticOffsetPixels: Float,
                        rawOffset: Float, rawOffsetPixels: Float) {
    }

    /**
     * Called when dragging is released and has exceeded the threshold dismiss distance.
     */
    fun onDragDismissed() {}

}