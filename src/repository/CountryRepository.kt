package com.ehtesham.repository

import com.ehtesham.entity.Country

interface CountryRepository {

    fun getAllCountries(): List<Country>

    fun getCountry(countryCode: String): Country?

    fun saveCountries(countries: List<Country>)
}