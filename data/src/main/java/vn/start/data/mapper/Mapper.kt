package vn.start.data.mapper

interface Mapper<E, M> {
    fun fromEntity(entity: E): M
}