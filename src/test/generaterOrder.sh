#!/usr/bin/env bash

curl -X POST -H "Content-Type: application/json" -H "Cache-Control: no-cache" -H "Postman-Token: f46f11ac-cd91-6a31-1830-0a15fc97b5a5" -d '[
	{
    "sku": "xbkskksks",
    "name": "丹参滴丸",
    "desc": "很好用",
    "price": 20.01
	},
	{
    "sku": "xbkskksks",
    "name": "丹参滴丸1",
    "desc": "很好用1",
    "price": 10.01
	},
	{
    "sku": "xbkskksks",
    "name": "丹参滴丸2",
    "desc": "很好用2",
    "price": 30.01
	}
]
' "http://127.0.0.1:8080/api/order/1"