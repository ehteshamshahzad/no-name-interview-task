package com.ehtesham.database

import org.ktorm.entity.Entity
import org.ktorm.schema.Table
import org.ktorm.schema.varchar

object DBCountryTable : Table<DBCountryEntity>("country") {
    val countryCode = varchar("country_code").primaryKey().bindTo { it.countryCode }
    val fullName = varchar("full_name").bindTo { it.fullName }
}

interface DBCountryEntity : Entity<DBCountryEntity> {
    companion object : Entity.Factory<DBCountryEntity>()
    val countryCode: String
    val fullName: String
}