FORMAT: 1A
HOST:localhost

# ESI14-RentIt
 RentIt's API

# Group Purchase Orders
Notes related resources of the **purchase orders API**

## Plant Catalog [/rest/plants]
### Retrieve All Plants [GET]

+ Response 200 (application/json)

        [
            {"links":[{"rel":"self", "href":"http://localhost:9000/rest/plants/10001"}], "name":"Excavator", "description":"1.5 Tonne Mini excavator", "price":150.00},
            {"links":[{"rel":"self", "href":"http://localhost:9000/rest/plants/10002"}], "name":"Excavator", "description":"3 Tonne Mini excavator", "price":200.00},
            {"links":[{"rel":"self", "href":"http://localhost:9000/rest/plants/10003"}], "name":"Excavator", "description":"5 Tonne Midi excavator", "price":250.00},
            {"links":[{"rel":"self", "href":"http://localhost:9000/rest/plants/10004"}], "name":"Excavator", "description":"8 Tonne Midi excavator", "price":300.00},
            {"links":[{"rel":"self", "href":"http://localhost:9000/rest/plants/10005"}], "name":"Excavator", "description":"15 Tonne Large excavator", "price":400.00},
            {"links":[{"rel":"self", "href":"http://localhost:9000/rest/plants/10006"}], "name":"Excavator", "description":"20 Tonne Large excavator", "price":450.00}
        ]

## Plant Catalog [/rest/plants/]
### Create a Plant [POST]

+ Request (application/json)

        {"name":"Fire","description":"fat","price":"145"}

+ Response 200 (application/json)

        [        
            {
                "_links": 
                [
                    {
                        "rel": "updatePlant",
                        "href": "http://localhost:8070/rest/plants/1/update",
                        "method": "PUT"
                    },
                    {
                        "rel": "deletePlant",
                        "href": "http://localhost:8070/rest/plants/1/delete",
                        "method": "DELETE"
                    }
                ],
                "name": "Excavator",
                "description": "Heavy",
                "price": 400,
                "links": 
                [
                    {
                        "rel": "self",
                        "href": "http://localhost:8070/rest/plants/1"
                    }
                ]
            },
            {
                "_links": 
            [
                    {
                        "rel": "updatePlant",
                        "href": "http://localhost:8070/rest/plants/2/update",
                        "method": "PUT"
                    },
                    {
                        "rel": "deletePlant",
                        "href": "http://localhost:8070/rest/plants/2/delete",
                        "method": "DELETE"
                    }
                ],
                "name": "Fire",
                "description": "fat",
                "price": 145,
                "links": [
                    {
                        "rel": "self",
                        "href": "http://localhost:8070/rest/plants/2"
                    }
                ]
            }
        ]


## Purchase Order Management [/rest/pos/id/update]
### Update Purchase Order [PUT]
+ Response 200 (application/json)

        {
         "_links": [
        {
            "rel": "updatePlant",
            "href": "http://localhost:8070/rest/plants/1/update",
            "method": "PUT"
        },
        {
            "rel": "deletePlant",
            "href": "http://localhost:8070/rest/plants/1/delete",
            "method": "DELETE"
        }
        ],
        "name": "Cigin",
        "description": "Wow",
        "price": 200,
        "links": [
        {
            "rel": "self",
            "href": "http://localhost:8070/rest/plants/1"
        }
            ]
        }

## Purchase Order Creation [/rest/pos/]
### Create Purchase Order [POST]
+ Request (application/json)

        {
            "plant":{
                "links":[
                    { "rel":"self", "href":"http://localhost:3000/rest/plants/10001" }
                ]
            },
            "startDate":"2014-11-12",
            "endDate":"2014-11-14"
        }


+ Response 201 (application/json)

        {
            "links":[
                { "rel":"self", "href":"http://localhost:3000/rest/pos/10001" }
            ],
            "_links":[
                { "rel":"closePO", "href":"http://localhost:3000/rest/pos/10001", "method":"DELETE" }

            ],
            
            "plant":{
                "links":[
                    { "rel":"self", "href":"http://localhost:3000/rest/plants/10001" }
                ],
                "_links":[]
            },
            "startDate":"2014-11-12",
            "endDate":"2014-11-14",
            "cost":750.0
        }
        
+ Response 409 (application/json)

        {
            "links":[
                { "rel":"self", "href":"http://localhost:3000/rest/pos/10001" }
            ],
            "_links":[
                { "rel":"updatePO", "href":"http://localhost:3000/rest/pos/10001", "method":"PUT" }
            ],
            "plant":{
                "links":[
                    { "rel":"self", "href":"http://localhost:3000/rest/plants/10001" }
                ],
                "_links":[]
            },
            "startDate":"2014-11-12",
            "endDate":"2014-11-14"
        }


        
## Purchase Order Management [/rest/pos/id]
### Retrieve Purchase Order ID [GET]  
+ Response 200 (application/json)

        [{
            "links":[
                { "rel":"self", "href":"http://localhost:3000/rest/pos/10001" }
            ],
            "_links":[
                { "rel":"closePO", "href":"http://localhost:3000/rest/pos/10001", "method":"DELETE" }
                ],
            
            "_links":[
                {"rel":"updatePO", "href":"http://localhost:3000/rest/pos/10001", "method":"PUT"}
                ],
            "plant":{
                "links":[
                    { "rel":"self", "href":"http://localhost:3000/rest/plants/10001" }
                ],
                "_links":[]
            },
            "startDate":"2014-11-12",
            "endDate":"2014-11-14",
            "cost":750.0
        }]
        
## Purchase Order Management [/rest/pos/id/update/]
### Update Purchase Order [PUT]

+ Response 200 (application/json)

        [{
            "links":[
                { "rel":"self", "href":"http://localhost:3000/rest/pos/10001" }
            ],
            "_links":[
                { "rel":"closePO", "href":"http://localhost:3000/rest/pos/10001", "method":"DELETE" }
               
            ],
            "plant":{
                "links":[
                    { "rel":"self", "href":"http://localhost:3000/rest/plants/10001" }
                ],
                "_links":[]
            },
            "startDate":"2014-11-12",
            "endDate":"2014-11-14",
            "cost":750.0
        }]
        
## Purchase Order Management [/rest/pos/id/]
### Update Purchase Order [DELETE]
+ Response 200 (application/json)