package vn.start.common

/**
 * Generic mapper interface for transforming between entity and domain model types.
 * @param E Entity type (DTO/Database entity)
 * @param M Model type (Domain model)
 */
interface Mapper<E, M> {
    fun fromEntity(entity: E): M
    fun toEntity(model: M): E
}

/**
 * One-way mapper for cases where reverse mapping is not needed.
 */
interface OneWayMapper<E, M> {
    fun fromEntity(entity: E): M
}


