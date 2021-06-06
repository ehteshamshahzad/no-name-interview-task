package com.ehtesham.repository

import com.ehtesham.entity.Country

var countryStorage = mutableListOf<Country>()

class InMemoryCountryRepository : CountryRepository {

    override fun getAllCountries(): List<Country> {
        return countryStorage
    }

    override fun getCountry(countryCode: String): Country? {
        return countryStorage.firstOrNull { it.countryCode == countryCode }
    }

    override fun saveCountries(countries: List<Country>) {
        for (country in countries){
            countryStorage.add(country)
        }
    }
}