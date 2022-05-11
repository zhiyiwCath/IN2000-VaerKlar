package no.uio.g19.klesapp.test

import no.uio.g19.klesapp.data.api.ApiInterface
import com.google.gson.Gson
import no.uio.g19.klesapp.data.model.geocoder.GeocoderAddress
import no.uio.g19.klesapp.data.model.locationforecast.Locationforecast
import no.uio.g19.klesapp.data.model.nowcast.Nowcast
import no.uio.g19.klesapp.data.model.sunrise.Sunrise

/**
 * Simple API-stub to get consistent responses to enable reproducible tests.
 *
 * @see ApiInterface
 */
class ApiDummyStub : ApiInterface {
    private val testDataNowcast = "{\n" +
            "  \"type\": \"Feature\",\n" +
            "  \"geometry\": {\n" +
            "    \"type\": \"Point\",\n" +
            "    \"coordinates\": [\n" +
            "      10.7579,\n" +
            "      59.9114,\n" +
            "      3\n" +
            "    ]\n" +
            "  },\n" +
            "  \"properties\": {\n" +
            "    \"meta\": {\n" +
            "      \"updated_at\": \"2021-04-21T09:12:20Z\",\n" +
            "      \"units\": {\n" +
            "        \"air_temperature\": \"celsius\",\n" +
            "        \"precipitation_amount\": \"mm\",\n" +
            "        \"precipitation_rate\": \"mm/h\",\n" +
            "        \"relative_humidity\": \"%\",\n" +
            "        \"wind_from_direction\": \"degrees\",\n" +
            "        \"wind_speed\": \"m/s\",\n" +
            "        \"wind_speed_of_gust\": \"m/s\"\n" +
            "      },\n" +
            "      \"radar_coverage\": \"ok\"\n" +
            "    },\n" +
            "    \"timeseries\": [\n" +
            "      {\n" +
            "        \"time\": \"2021-04-21T09:15:00Z\",\n" +
            "        \"data\": {\n" +
            "          \"instant\": {\n" +
            "            \"details\": {\n" +
            "              \"air_temperature\": 9.2,\n" +
            "              \"precipitation_rate\": 0.3,\n" +
            "              \"relative_humidity\": 48,\n" +
            "              \"wind_from_direction\": -31.2,\n" +
            "              \"wind_speed\": 4.8,\n" +
            "              \"wind_speed_of_gust\": 9.4\n" +
            "            }\n" +
            "          },\n" +
            "          \"next_1_hours\": {\n" +
            "            \"summary\": {\n" +
            "              \"symbol_code\": \"lightrain\"\n" +
            "            },\n" +
            "            \"details\": {\n" +
            "              \"precipitation_amount\": 0.2\n" +
            "            }\n" +
            "          }\n" +
            "        }\n" +
            "      },\n" +
            "      {\n" +
            "        \"time\": \"2021-04-21T09:20:00Z\",\n" +
            "        \"data\": {\n" +
            "          \"instant\": {\n" +
            "            \"details\": {\n" +
            "              \"precipitation_rate\": 0.4\n" +
            "            }\n" +
            "          }\n" +
            "        }\n" +
            "      },\n" +
            "      {\n" +
            "        \"time\": \"2021-04-21T09:25:00Z\",\n" +
            "        \"data\": {\n" +
            "          \"instant\": {\n" +
            "            \"details\": {\n" +
            "              \"precipitation_rate\": 0.3\n" +
            "            }\n" +
            "          }\n" +
            "        }\n" +
            "      },\n" +
            "      {\n" +
            "        \"time\": \"2021-04-21T09:30:00Z\",\n" +
            "        \"data\": {\n" +
            "          \"instant\": {\n" +
            "            \"details\": {\n" +
            "              \"precipitation_rate\": 0.2\n" +
            "            }\n" +
            "          }\n" +
            "        }\n" +
            "      },\n" +
            "      {\n" +
            "        \"time\": \"2021-04-21T09:35:00Z\",\n" +
            "        \"data\": {\n" +
            "          \"instant\": {\n" +
            "            \"details\": {\n" +
            "              \"precipitation_rate\": 0.1\n" +
            "            }\n" +
            "          }\n" +
            "        }\n" +
            "      },\n" +
            "      {\n" +
            "        \"time\": \"2021-04-21T09:40:00Z\",\n" +
            "        \"data\": {\n" +
            "          \"instant\": {\n" +
            "            \"details\": {\n" +
            "              \"precipitation_rate\": 0.1\n" +
            "            }\n" +
            "          }\n" +
            "        }\n" +
            "      },\n" +
            "      {\n" +
            "        \"time\": \"2021-04-21T09:45:00Z\",\n" +
            "        \"data\": {\n" +
            "          \"instant\": {\n" +
            "            \"details\": {\n" +
            "              \"precipitation_rate\": 0.1\n" +
            "            }\n" +
            "          }\n" +
            "        }\n" +
            "      },\n" +
            "      {\n" +
            "        \"time\": \"2021-04-21T09:50:00Z\",\n" +
            "        \"data\": {\n" +
            "          \"instant\": {\n" +
            "            \"details\": {\n" +
            "              \"precipitation_rate\": 0.1\n" +
            "            }\n" +
            "          }\n" +
            "        }\n" +
            "      },\n" +
            "      {\n" +
            "        \"time\": \"2021-04-21T09:55:00Z\",\n" +
            "        \"data\": {\n" +
            "          \"instant\": {\n" +
            "            \"details\": {\n" +
            "              \"precipitation_rate\": 0.1\n" +
            "            }\n" +
            "          }\n" +
            "        }\n" +
            "      },\n" +
            "      {\n" +
            "        \"time\": \"2021-04-21T10:00:00Z\",\n" +
            "        \"data\": {\n" +
            "          \"instant\": {\n" +
            "            \"details\": {\n" +
            "              \"precipitation_rate\": 0.1\n" +
            "            }\n" +
            "          }\n" +
            "        }\n" +
            "      },\n" +
            "      {\n" +
            "        \"time\": \"2021-04-21T10:05:00Z\",\n" +
            "        \"data\": {\n" +
            "          \"instant\": {\n" +
            "            \"details\": {\n" +
            "              \"precipitation_rate\": 0.1\n" +
            "            }\n" +
            "          }\n" +
            "        }\n" +
            "      },\n" +
            "      {\n" +
            "        \"time\": \"2021-04-21T10:10:00Z\",\n" +
            "        \"data\": {\n" +
            "          \"instant\": {\n" +
            "            \"details\": {\n" +
            "              \"precipitation_rate\": 0.1\n" +
            "            }\n" +
            "          }\n" +
            "        }\n" +
            "      },\n" +
            "      {\n" +
            "        \"time\": \"2021-04-21T10:15:00Z\",\n" +
            "        \"data\": {\n" +
            "          \"instant\": {\n" +
            "            \"details\": {\n" +
            "              \"precipitation_rate\": 0.1\n" +
            "            }\n" +
            "          }\n" +
            "        }\n" +
            "      },\n" +
            "      {\n" +
            "        \"time\": \"2021-04-21T10:20:00Z\",\n" +
            "        \"data\": {\n" +
            "          \"instant\": {\n" +
            "            \"details\": {\n" +
            "              \"precipitation_rate\": 0.1\n" +
            "            }\n" +
            "          }\n" +
            "        }\n" +
            "      },\n" +
            "      {\n" +
            "        \"time\": \"2021-04-21T10:25:00Z\",\n" +
            "        \"data\": {\n" +
            "          \"instant\": {\n" +
            "            \"details\": {\n" +
            "              \"precipitation_rate\": 0.1\n" +
            "            }\n" +
            "          }\n" +
            "        }\n" +
            "      },\n" +
            "      {\n" +
            "        \"time\": \"2021-04-21T10:30:00Z\",\n" +
            "        \"data\": {\n" +
            "          \"instant\": {\n" +
            "            \"details\": {\n" +
            "              \"precipitation_rate\": 0.1\n" +
            "            }\n" +
            "          }\n" +
            "        }\n" +
            "      },\n" +
            "      {\n" +
            "        \"time\": \"2021-04-21T10:35:00Z\",\n" +
            "        \"data\": {\n" +
            "          \"instant\": {\n" +
            "            \"details\": {\n" +
            "              \"precipitation_rate\": 0.1\n" +
            "            }\n" +
            "          }\n" +
            "        }\n" +
            "      },\n" +
            "      {\n" +
            "        \"time\": \"2021-04-21T10:40:00Z\",\n" +
            "        \"data\": {\n" +
            "          \"instant\": {\n" +
            "            \"details\": {\n" +
            "              \"precipitation_rate\": 0.1\n" +
            "            }\n" +
            "          }\n" +
            "        }\n" +
            "      },\n" +
            "      {\n" +
            "        \"time\": \"2021-04-21T10:45:00Z\",\n" +
            "        \"data\": {\n" +
            "          \"instant\": {\n" +
            "            \"details\": {\n" +
            "              \"precipitation_rate\": 0.1\n" +
            "            }\n" +
            "          }\n" +
            "        }\n" +
            "      },\n" +
            "      {\n" +
            "        \"time\": \"2021-04-21T10:50:00Z\",\n" +
            "        \"data\": {\n" +
            "          \"instant\": {\n" +
            "            \"details\": {\n" +
            "              \"precipitation_rate\": 0.2\n" +
            "            }\n" +
            "          }\n" +
            "        }\n" +
            "      },\n" +
            "      {\n" +
            "        \"time\": \"2021-04-21T10:55:00Z\",\n" +
            "        \"data\": {\n" +
            "          \"instant\": {\n" +
            "            \"details\": {\n" +
            "              \"precipitation_rate\": 0.2\n" +
            "            }\n" +
            "          }\n" +
            "        }\n" +
            "      },\n" +
            "      {\n" +
            "        \"time\": \"2021-04-21T11:00:00Z\",\n" +
            "        \"data\": {\n" +
            "          \"instant\": {\n" +
            "            \"details\": {\n" +
            "              \"precipitation_rate\": 0.2\n" +
            "            }\n" +
            "          }\n" +
            "        }\n" +
            "      },\n" +
            "      {\n" +
            "        \"time\": \"2021-04-21T11:05:00Z\",\n" +
            "        \"data\": {\n" +
            "          \"instant\": {\n" +
            "            \"details\": {\n" +
            "              \"precipitation_rate\": 0.2\n" +
            "            }\n" +
            "          }\n" +
            "        }\n" +
            "      }\n" +
            "    ]\n" +
            "  }\n" +
            "}"
    
    private val testDataLocationforecast = """{
        "properties": {
        "meta": {
        "units": {
        "air_temperature_max": "C",
        "precipitation_amount_max": "mm",
        "cloud_area_fraction_low": "%",
        "wind_speed": "m/s",
        "relative_humidity": "%",
        "probability_of_precipitation": "%",
        "air_temperature": "C",
        "precipitation_amount": "mm",
        "wind_from_direction": "degrees",
        "dew_point_temperature": "C",
        "fog_area_fraction": "%",
        "probability_of_thunder": "%",
        "air_pressure_at_sea_level": "hPa",
        "ultraviolet_index_clear_sky_max": "1",
        "precipitation_amount_min": "mm",
        "air_temperature_min": "C",
        "wind_speed_of_gust": "m/s",
        "cloud_area_fraction": "%",
        "cloud_area_fraction_high": "%",
        "cloud_area_fraction_medium": "%"
    },
        "updated_at": "2019-12-03T13:52:13Z"
    },
        "timeseries": [
        {
            "data": {
            "instant": {
            "details": {
            "dew_point_temperature": 8.1,
            "fog_area_fraction": 95.2,
            "cloud_area_fraction_low": 95.2,
            "wind_from_direction": 121.3,
            "air_pressure_at_sea_level": 1017.23,
            "relative_humidity": 81.1,
            "wind_speed": 5.9,
            "wind_speed_of_gust": 15.9,
            "cloud_area_fraction": 95.2,
            "cloud_area_fraction_medium": 95.2,
            "air_temperature": 17.1,
            "cloud_area_fraction_high": 95.2
        }
        },
            "next_12_hours": {
            "summary": {
            "symbol_code": "clearsky_day"
        },
            "details": {
            "ultraviolet_index_clear_sky_max": 1,
            "probability_of_precipitation": 37,
            "probability_of_thunder": 54.32,
            "precipitation_amount_max": 4.32,
            "air_temperature_max": 17.1,
            "precipitation_amount": 1.71,
            "air_temperature_min": 11.1,
            "precipitation_amount_min": 4.32
        }
        },
            "next_1_hours": {
            "summary": {
            "symbol_code": "clearsky_day"
        },
            "details": {
            "ultraviolet_index_clear_sky_max": 1,
            "probability_of_precipitation": 37,
            "probability_of_thunder": 54.32,
            "precipitation_amount_max": 4.32,
            "air_temperature_max": 17.1,
            "precipitation_amount": 1.71,
            "air_temperature_min": 11.1,
            "precipitation_amount_min": 4.32
        }
        },
            "next_6_hours": {
            "summary": {
            "symbol_code": "clearsky_day"
        },
            "details": {
            "ultraviolet_index_clear_sky_max": 1,
            "probability_of_precipitation": 37,
            "probability_of_thunder": 54.32,
            "precipitation_amount_max": 4.32,
            "air_temperature_max": 17.1,
            "precipitation_amount": 1.71,
            "air_temperature_min": 11.1,
            "precipitation_amount_min": 4.32
        }
        }
        },
            "time": "2019-12-03T14:00:00Z"
        }
        ]
    },
        "geometry": {
        "coordinates": [
        60.5,
        11.59,
        1001
        ],
        "type": "Point"
    },
        "type": "Feature"
    }"""

    private val testDataSunrise = """ {"location":{"time":[{"moonphase":{"value":"75.420157547","desc":"LOCAL MOON STATE * MOON PHASE= 75.4 (waning crescent)","time":"2021-05-04T00:00:00+01:00"},"sunset":{"desc":"LOCAL DIURNAL SUN SET","time":"2021-05-04T20:22:13+01:00"},"moonshadow":{"time":"2021-05-04T00:00:00+01:00","desc":"LOCAL MOON STATE * SHADOW ANGLES (azi=255.3,ele=-1.7)","elevation":"-1.682382793","azimuth":"255.337722517"},"solarnoon":{"desc":"LOCAL DIURNAL MAXIMUM SOLAR ELEVATION (Max= 46.20021)","elevation":"46.200206292","time":"2021-05-04T12:14:13+01:00"},"sunrise":{"time":"2021-05-04T04:07:26+01:00","desc":"LOCAL DIURNAL SUN RISE"},"date":"2021-05-04","moonposition":{"range":"386388.61222715","time":"2021-05-04T00:00:00+01:00","phase":"75.420157547","desc":"LOCAL MOON POSITION Elv: -24.446 deg, Azi: 91.001, Rng: 386388.6 km","elevation":"-24.445649673","azimuth":"91.001318946"},"low_moon":{"elevation":"-48.118431162","desc":"LOCAL DIURNAL MINIMUM MOON ELEVATION (Min= -48.11843)","time":"2021-05-04T19:21:27+01:00"},"high_moon":{"elevation":"9.745841114","desc":"LOCAL DIURNAL MAXIMUM MOON ELEVATION (Max= 9.74584)","time":"2021-05-04T07:05:54+01:00"},"solarmidnight":{"time":"2021-05-04T00:13:38+01:00","elevation":"-14.14486872","desc":"LOCAL DIURNAL MINIMUM SOLAR ELEVATION (Min= -14.14487)"},"moonrise":{"desc":"LOCAL DIURNAL MOON RISE","time":"2021-05-04T03:32:34+01:00"},"moonset":{"desc":"LOCAL DIURNAL MOON SET","time":"2021-05-04T10:39:56+01:00"}},{"date":"2021-05-05","moonposition":{"time":"2021-05-05T00:00:00+01:00","phase":"78.764508957","range":"392014.890627486","desc":"LOCAL MOON POSITION Elv: -27.110 deg, Azi: 78.024, Rng: 392014.9 km","azimuth":"78.024055902","elevation":"-27.109821763"}}],"longitude":"10.7","latitude":"59.9","height":"0"},"meta":{"licenseurl":"https://api.met.no/license_data.html"}} """

    private val testDataGeocoder = """{
        "plus_code" : {
        "compound_code" : "WQ65+H5 Oslo, Norway",
        "global_code" : "9FFGWQ65+H5"
    },
        "results" : [
        {
            "address_components" : [
            {
                "long_name" : "10",
                "short_name" : "10",
                "types" : [ "street_number" ]
            },
            {
                "long_name" : "Schweigaards gate",
                "short_name" : "Schweigaards gate",
                "types" : [ "route" ]
            },
            {
                "long_name" : "Gamle Oslo",
                "short_name" : "Gamle Oslo",
                "types" : [ "political", "sublocality", "sublocality_level_1" ]
            },
            {
                "long_name" : "Oslo",
                "short_name" : "Oslo",
                "types" : [ "postal_town" ]
            },
            {
                "long_name" : "Oslo kommune",
                "short_name" : "Oslo kommune",
                "types" : [ "administrative_area_level_2", "political" ]
            },
            {
                "long_name" : "Oslo",
                "short_name" : "Oslo",
                "types" : [ "administrative_area_level_1", "political" ]
            },
            {
                "long_name" : "Norway",
                "short_name" : "NO",
                "types" : [ "country", "political" ]
            },
            {
                "long_name" : "0185",
                "short_name" : "0185",
                "types" : [ "postal_code" ]
            }
            ],
            "formatted_address" : "Schweigaards gate 10, 0185 Oslo, Norway",
            "geometry" : {
            "location" : {
            "lat" : 59.9116799,
            "lng" : 10.7581479
        },
            "location_type" : "ROOFTOP",
            "viewport" : {
            "northeast" : {
            "lat" : 59.9130288802915,
            "lng" : 10.7594968802915
        },
            "southwest" : {
            "lat" : 59.91033091970849,
            "lng" : 10.7567989197085
        }
        }
        },
            "place_id" : "ChIJhSbbHmBuQUYRW6aH9nF5obM",
            "plus_code" : {
            "compound_code" : "WQ65+M7 Oslo, Norway",
            "global_code" : "9FFGWQ65+M7"
        },
            "types" : [ "street_address" ]
        },
        {
            "address_components" : [
            {
                "long_name" : "13",
                "short_name" : "13",
                "types" : [ "street_number" ]
            },
            {
                "long_name" : "Ring1",
                "short_name" : "Ring1",
                "types" : [ "route" ]
            },
            {
                "long_name" : "Sentrum",
                "short_name" : "Sentrum",
                "types" : [ "political", "sublocality", "sublocality_level_1" ]
            },
            {
                "long_name" : "Oslo",
                "short_name" : "Oslo",
                "types" : [ "postal_town" ]
            },
            {
                "long_name" : "Oslo kommune",
                "short_name" : "Oslo kommune",
                "types" : [ "administrative_area_level_2", "political" ]
            },
            {
                "long_name" : "Oslo",
                "short_name" : "Oslo",
                "types" : [ "administrative_area_level_1", "political" ]
            },
            {
                "long_name" : "Norway",
                "short_name" : "NO",
                "types" : [ "country", "political" ]
            },
            {
                "long_name" : "0185",
                "short_name" : "0185",
                "types" : [ "postal_code" ]
            }
            ],
            "formatted_address" : "Ring1 13, 0185 Oslo, Norway",
            "geometry" : {
            "location" : {
            "lat" : 59.9114045,
            "lng" : 10.7582036
        },
            "location_type" : "RANGE_INTERPOLATED",
            "viewport" : {
            "northeast" : {
            "lat" : 59.9127534802915,
            "lng" : 10.7595525802915
        },
            "southwest" : {
            "lat" : 59.91005551970849,
            "lng" : 10.7568546197085
        }
        }
        },
            "place_id" : "EhtSaW5nMSAxMywgMDE4NSBPc2xvLCBOb3J3YXkiGhIYChQKEgkvydUgYG5BRhFAXYwyKmBiZhAN",
            "types" : [ "street_address" ]
        },
        {
            "address_components" : [
            {
                "long_name" : "Riksveg 162",
                "short_name" : "Rv162",
                "types" : [ "route" ]
            },
            {
                "long_name" : "Gamle Oslo",
                "short_name" : "Gamle Oslo",
                "types" : [ "political", "sublocality", "sublocality_level_1" ]
            },
            {
                "long_name" : "Oslo",
                "short_name" : "Oslo",
                "types" : [ "postal_town" ]
            },
            {
                "long_name" : "Oslo kommune",
                "short_name" : "Oslo kommune",
                "types" : [ "administrative_area_level_2", "political" ]
            },
            {
                "long_name" : "Oslo",
                "short_name" : "Oslo",
                "types" : [ "administrative_area_level_1", "political" ]
            },
            {
                "long_name" : "Norway",
                "short_name" : "NO",
                "types" : [ "country", "political" ]
            },
            {
                "long_name" : "0185",
                "short_name" : "0185",
                "types" : [ "postal_code" ]
            }
            ],
            "formatted_address" : "Rv162, 0185 Oslo, Norway",
            "geometry" : {
            "bounds" : {
            "northeast" : {
            "lat" : 59.911467,
            "lng" : 10.7580009
        },
            "southwest" : {
            "lat" : 59.9113463,
            "lng" : 10.7578274
        }
        },
            "location" : {
            "lat" : 59.911412,
            "lng" : 10.7579286
        },
            "location_type" : "GEOMETRIC_CENTER",
            "viewport" : {
            "northeast" : {
            "lat" : 59.91275563029151,
            "lng" : 10.7592631302915
        },
            "southwest" : {
            "lat" : 59.91005766970849,
            "lng" : 10.7565651697085
        }
        }
        },
            "place_id" : "ChIJH1wzHmBuQUYRyLCu06ypUZI",
            "types" : [ "route" ]
        },
        {
            "address_components" : [
            {
                "long_name" : "0185",
                "short_name" : "0185",
                "types" : [ "postal_code" ]
            },
            {
                "long_name" : "Oslo",
                "short_name" : "Oslo",
                "types" : [ "postal_town" ]
            },
            {
                "long_name" : "Oslo Municipality",
                "short_name" : "Oslo Municipality",
                "types" : [ "administrative_area_level_2", "political" ]
            },
            {
                "long_name" : "Oslo",
                "short_name" : "Oslo",
                "types" : [ "administrative_area_level_1", "political" ]
            },
            {
                "long_name" : "Norway",
                "short_name" : "NO",
                "types" : [ "country", "political" ]
            }
            ],
            "formatted_address" : "0185 Oslo, Norway",
            "geometry" : {
            "bounds" : {
            "northeast" : {
            "lat" : 59.9134109,
            "lng" : 10.7607978
        },
            "southwest" : {
            "lat" : 59.91069309999999,
            "lng" : 10.7534232
        }
        },
            "location" : {
            "lat" : 59.9118518,
            "lng" : 10.7560848
        },
            "location_type" : "APPROXIMATE",
            "viewport" : {
            "northeast" : {
            "lat" : 59.9134109,
            "lng" : 10.7607978
        },
            "southwest" : {
            "lat" : 59.91069309999999,
            "lng" : 10.7534232
        }
        }
        },
            "place_id" : "ChIJsTX7OWBuQUYRwY6fHHl4ayo",
            "types" : [ "postal_code" ]
        },
        {
            "address_components" : [
            {
                "long_name" : "Gamle Oslo",
                "short_name" : "Gamle Oslo",
                "types" : [ "political", "sublocality", "sublocality_level_1" ]
            },
            {
                "long_name" : "Oslo",
                "short_name" : "Oslo",
                "types" : [ "locality", "political" ]
            },
            {
                "long_name" : "Oslo",
                "short_name" : "Oslo",
                "types" : [ "administrative_area_level_1", "political" ]
            },
            {
                "long_name" : "Norway",
                "short_name" : "NO",
                "types" : [ "country", "political" ]
            }
            ],
            "formatted_address" : "Gamle Oslo, Oslo, Norway",
            "geometry" : {
            "bounds" : {
            "northeast" : {
            "lat" : 59.9274235,
            "lng" : 10.8337214
        },
            "southwest" : {
            "lat" : 59.8784709,
            "lng" : 10.6903508
        }
        },
            "location" : {
            "lat" : 59.90677520000001,
            "lng" : 10.7622822
        },
            "location_type" : "APPROXIMATE",
            "viewport" : {
            "northeast" : {
            "lat" : 59.9274235,
            "lng" : 10.8337214
        },
            "southwest" : {
            "lat" : 59.8784709,
            "lng" : 10.6903508
        }
        }
        },
            "place_id" : "ChIJZ1tA7FVuQUYRXNeQ7WYN1RA",
            "types" : [ "political", "sublocality", "sublocality_level_1" ]
        },
        {
            "address_components" : [
            {
                "long_name" : "Oslo",
                "short_name" : "Oslo",
                "types" : [ "locality", "political" ]
            },
            {
                "long_name" : "Oslo",
                "short_name" : "Oslo",
                "types" : [ "administrative_area_level_1", "political" ]
            },
            {
                "long_name" : "Norway",
                "short_name" : "NO",
                "types" : [ "country", "political" ]
            }
            ],
            "formatted_address" : "Oslo, Norway",
            "geometry" : {
            "bounds" : {
            "northeast" : {
            "lat" : 59.978035,
            "lng" : 10.9476641
        },
            "southwest" : {
            "lat" : 59.8096749,
            "lng" : 10.6225689
        }
        },
            "location" : {
            "lat" : 59.9138688,
            "lng" : 10.7522454
        },
            "location_type" : "APPROXIMATE",
            "viewport" : {
            "northeast" : {
            "lat" : 59.978035,
            "lng" : 10.9476641
        },
            "southwest" : {
            "lat" : 59.8096749,
            "lng" : 10.6225689
        }
        }
        },
            "place_id" : "ChIJOfBn8mFuQUYRmh4j019gkn4",
            "types" : [ "locality", "political" ]
        },
        {
            "address_components" : [
            {
                "long_name" : "Oslo",
                "short_name" : "Oslo",
                "types" : [ "administrative_area_level_1", "political" ]
            },
            {
                "long_name" : "Norway",
                "short_name" : "NO",
                "types" : [ "country", "political" ]
            }
            ],
            "formatted_address" : "Oslo, Norway",
            "geometry" : {
            "bounds" : {
            "northeast" : {
            "lat" : 60.1351062,
            "lng" : 10.9513895
        },
            "southwest" : {
            "lat" : 59.8093113,
            "lng" : 10.4891652
        }
        },
            "location" : {
            "lat" : 59.984904,
            "lng" : 10.7166777
        },
            "location_type" : "APPROXIMATE",
            "viewport" : {
            "northeast" : {
            "lat" : 60.1351062,
            "lng" : 10.9513895
        },
            "southwest" : {
            "lat" : 59.8093113,
            "lng" : 10.4891652
        }
        }
        },
            "place_id" : "ChIJvRIImcJtQUYReQpUuAPqbBA",
            "types" : [ "administrative_area_level_1", "political" ]
        },
        {
            "address_components" : [
            {
                "long_name" : "Oslo Municipality",
                "short_name" : "Oslo Municipality",
                "types" : [ "administrative_area_level_2", "political" ]
            },
            {
                "long_name" : "Oslo",
                "short_name" : "Oslo",
                "types" : [ "administrative_area_level_1", "political" ]
            },
            {
                "long_name" : "Norway",
                "short_name" : "NO",
                "types" : [ "country", "political" ]
            }
            ],
            "formatted_address" : "Oslo Municipality, Norway",
            "geometry" : {
            "bounds" : {
            "northeast" : {
            "lat" : 60.1351062,
            "lng" : 10.9513895
        },
            "southwest" : {
            "lat" : 59.8093113,
            "lng" : 10.4891652
        }
        },
            "location" : {
            "lat" : 59.91418460000001,
            "lng" : 10.7524098
        },
            "location_type" : "APPROXIMATE",
            "viewport" : {
            "northeast" : {
            "lat" : 60.1351062,
            "lng" : 10.9513895
        },
            "southwest" : {
            "lat" : 59.8093113,
            "lng" : 10.4891652
        }
        }
        },
            "place_id" : "ChIJ-S3gPBpyQUYRxREh8AJu3Gw",
            "types" : [ "administrative_area_level_2", "political" ]
        },
        {
            "address_components" : [
            {
                "long_name" : "Oslo",
                "short_name" : "Oslo",
                "types" : [ "postal_town" ]
            },
            {
                "long_name" : "Norway",
                "short_name" : "NO",
                "types" : [ "country", "political" ]
            },
            {
                "long_name" : "0890",
                "short_name" : "0890",
                "types" : [ "postal_code" ]
            }
            ],
            "formatted_address" : "0890 Oslo, Norway",
            "geometry" : {
            "bounds" : {
            "northeast" : {
            "lat" : 60.172494,
            "lng" : 10.9513838
        },
            "southwest" : {
            "lat" : 59.8093521,
            "lng" : 10.4784964
        }
        },
            "location" : {
            "lat" : 59.984904,
            "lng" : 10.7166777
        },
            "location_type" : "APPROXIMATE",
            "viewport" : {
            "northeast" : {
            "lat" : 60.172494,
            "lng" : 10.9513838
        },
            "southwest" : {
            "lat" : 59.8093521,
            "lng" : 10.4784964
        }
        }
        },
            "place_id" : "ChIJi1cX3OeuRkYRmnsn_KZMJlM",
            "types" : [ "postal_town" ]
        },
        {
            "address_components" : [
            {
                "long_name" : "Norway",
                "short_name" : "NO",
                "types" : [ "country", "political" ]
            }
            ],
            "formatted_address" : "Norway",
            "geometry" : {
            "bounds" : {
            "northeast" : {
            "lat" : 71.30780000000001,
            "lng" : 31.3549999
        },
            "southwest" : {
            "lat" : 57.8097,
            "lng" : 4.0649
        }
        },
            "location" : {
            "lat" : 60.47202399999999,
            "lng" : 8.468945999999999
        },
            "location_type" : "APPROXIMATE",
            "viewport" : {
            "northeast" : {
            "lat" : 71.30780000000001,
            "lng" : 31.3549999
        },
            "southwest" : {
            "lat" : 57.8097,
            "lng" : 4.0649
        }
        }
        },
            "place_id" : "ChIJv-VNj0VoEkYRK9BkuJ07sKE",
            "types" : [ "country", "political" ]
        },
        {
            "address_components" : [
            {
                "long_name" : "WQ65+H5",
                "short_name" : "WQ65+H5",
                "types" : [ "plus_code" ]
            },
            {
                "long_name" : "Oslo",
                "short_name" : "Oslo",
                "types" : [ "locality", "political" ]
            },
            {
                "long_name" : "Oslo",
                "short_name" : "Oslo",
                "types" : [ "administrative_area_level_1", "political" ]
            },
            {
                "long_name" : "Norway",
                "short_name" : "NO",
                "types" : [ "country", "political" ]
            }
            ],
            "formatted_address" : "WQ65+H5 Oslo, Norway",
            "geometry" : {
            "bounds" : {
            "northeast" : {
            "lat" : 59.9115,
            "lng" : 10.758
        },
            "southwest" : {
            "lat" : 59.91137500000001,
            "lng" : 10.757875
        }
        },
            "location" : {
            "lat" : 59.9114,
            "lng" : 10.7579
        },
            "location_type" : "ROOFTOP",
            "viewport" : {
            "northeast" : {
            "lat" : 59.91278648029152,
            "lng" : 10.7592864802915
        },
            "southwest" : {
            "lat" : 59.9100885197085,
            "lng" : 10.7565885197085
        }
        }
        },
            "place_id" : "GhIJhslUwaj0TUAR3EYDeAuEJUA",
            "plus_code" : {
            "compound_code" : "WQ65+H5 Oslo, Norway",
            "global_code" : "9FFGWQ65+H5"
        },
            "types" : [ "plus_code" ]
        }
        ],
        "status" : "OK"
    }"""

    override suspend fun getNowcast(lat : Float, lon : Float) : Nowcast =
            Gson().fromJson(testDataNowcast, Nowcast::class.java)

    override suspend fun getLocationforecast(lat : Float, lon : Float) : Locationforecast =
            Gson().fromJson(testDataLocationforecast, Locationforecast::class.java)
    
    override suspend fun getSunrise(lat: Float, lon: Float, date: String, offset: String) : Sunrise =
            Gson().fromJson(testDataSunrise, Sunrise::class.java)

    override suspend fun getAddress(lat : Double, lon : Double) : GeocoderAddress =
        Gson().fromJson(testDataGeocoder, GeocoderAddress::class.java)
}
