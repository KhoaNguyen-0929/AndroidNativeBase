package vn.start.domain.repository

import kotlinx.coroutines.flow.Flow
import vn.start.domain.model.DemoModel

interface DemoRepository {
    fun demo() : Flow<DemoModel>
}