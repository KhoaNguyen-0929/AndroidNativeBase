package vn.start.data.mapper

interface Mapper<E, M> {
    fun entityToModel(entity: E): M
    fun modelToEntity(model: M): E
}