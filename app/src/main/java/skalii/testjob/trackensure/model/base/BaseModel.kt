package skalii.testjob.trackensure.model.base


interface BaseModel {
    val id: Int

    companion object {
        fun <Model : BaseModel> List<Model>.setData(data: List<Model>) {
            if (this is MutableList<Model>) {
                clear(); addAll(data)
            } else {
                toMutableList().apply { clear(); addAll(data) }
            }
        }
    }

}