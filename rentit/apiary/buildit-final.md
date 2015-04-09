FORMAT: 1A
HOST: http://localhost

# ESI14-BuildIt
Excerpt of BuildIt's API

# Group Plant Hire Request
Notes related resources of the **Plants API**




## Plant Hire Request Management [/rest/phr]
### Create Plant Hire Request [POST]
+ Request (application/json)

        {
            "plant":{
                "links":[
                    { "rel":"self", "href":"http://localhost:9000/rest/plants/10001" }
                ]
            },
            "startDate":"2014-11-12",
            "endDate":"2014-11-14",
        }

+ Response 201 (application/json)


        {
            "links":[
                    { "rel":"self", "href":"http://localhost:9000/rest/pos/10001" }
                ],
            "plant":{
                "links":[
                    { "rel":"self", "href":"http://localhost:9000/rest/plants/10001" }
                ]
            },
            "startDate":"2014-11-12",
            "endDate":"2014-11-14",
            "cost":500.00
        }

### Retrieve All Plant Hire Request [GET]

+ Response 200 (application/json)

        [  
        {  
          "plant":{  
             "name":"Vinod",
             "description":"1.5 tonn Dumper",
             "price":200.0,
             "links":[  
    
             ],
             "_links":[  
    
             ]
        },
          "price":200.0,
          "startDate":"2014-12-18",
          "endDate":"2014-12-18",
          "status":"PENDING_CONFIRMATION",
          "postatus":"PENDING_CREATE",
          "links":[  
             {  
                "rel":"self",
                "href":"http://localhost:8080/rest/phr/10"
             }
        ],
          "_links":[  
             {  
                "rel":"rejectPHR",
                "href":"http://localhost:8080/rest/phr/10/accept",
                "method":"DELETE"
             },
             {  
                "rel":"acceptPHR",
                "href":"http://localhost:8080/rest/phr/10/accept",
                "method":"POST"
             }
        ]
        },
        {  
          "plant":{  
             "name":"Vinod",
             "description":"1.5 tonn Dumper",
             "price":200.0,
             "links":[  
    
             ],
             "_links":[  
    
             ]
        },
          "price":200.0,
          "startDate":"2014-12-18",
          "endDate":"2014-12-18",
          "status":"PENDING_CONFIRMATION",
          "postatus":"PENDING_CREATE",
          "links":[  
             {  
                "rel":"self",
                "href":"http://localhost:8080/rest/phr/12"
             }
        ],
          "_links":[  
             {  
                "rel":"rejectPHR",
                "href":"http://localhost:8080/rest/phr/12/accept",
                "method":"DELETE"
             },
             {  
                "rel":"acceptPHR",
                "href":"http://localhost:8080/rest/phr/12/accept",
                "method":"POST"
             }
        ]
        },
        {  
          "plant":{  
             "name":"Vinod",
             "description":"1.5 tonn Dumper",
             "price":200.0,
             "links":[  
    
             ],
             "_links":[  
    
             ]
        },
          "price":200.0,
          "startDate":"2014-12-18",
          "endDate":"2014-12-18",
          "status":"PENDING_CONFIRMATION",
          "postatus":"PENDING_CREATE",
          "links":[  
             {  
                "rel":"self",
                "href":"http://localhost:8080/rest/phr/14"
             }
         ]
         }
        ]
          
          
## Plant Hire Request Operations [/rest/phr/id]
### PHR Retrieval [GET]

+ Response 200 (application/json)

        {
        "plant": {
            "name": "Vinod",
            "description": "1.5 tonn Dumper",
            "price": 200,
            "links": [],
            "_links": []
        },
        "price": 200,
        "startDate": "2014-12-18",
        "endDate": "2014-12-18",
        "status": "PENDING_CONFIRMATION",
        "postatus": "PENDING_CREATE",
        "links": [
            {
                "rel": "self",
                "href": "http://localhost:8080/rest/phr/2"
            }
        ],
        "_links": [
            {
                "rel": "rejectPHR",
                "href": "http://localhost:8080/rest/phr/2/accept",
                "method": "DELETE"
            },
            {
                "rel": "acceptPHR",
                "href": "http://localhost:8080/rest/phr/2/accept",
                "method": "POST"
            }
        ]
        }

## Plant Hire Request Acceptance Operations [/rest/phr/id/Accept]
### Plant Hire Request Acceptance [POST]

+ Response 200 (application/json)

        {
            "plant": {
            "name": "Vinod",
            "description": "1.5 tonn Dumper",
            "price": 200,
            "links": [],
            "_links": []
        },
                "price": 200,
                "startDate": "2014-12-18",
                "endDate": "2014-12-18",
                "status": "ACCEPTED",
                "postatus": "PENDING_CONFIRMATION",
                "links": [
            {
                "rel": "self",
                "href": "http://localhost:8080/rest/phr/2"
            }
            ],
                "_links": [
                {
                "rel": "closePHR",
                "href": "http://localhost:8080/rest/phr/2/close",
                "method": "POST"
            }
            ]
            }
            
## PHR Acceptance Operation [/rest/phr/id/accept] 
### Plant Hire Request Rejection [DELETE]

+ Response 200 (application/json)

        {
        "plant": {
        "name": "Vinod",
        "description": "1.5 tonn Dumper",
        "price": 200,
        "links": [],
        "_links": []
        },
        "price": 200,
        "startDate": "2014-12-18",
        "endDate": "2014-12-18",
        "status": "REJECT",
        "postatus": "PENDING_CONFIRMATION",
        "links": [
        {
        "rel": "self",
        "href": "http://localhost:8080/rest/phr/2"
        }
        ],
        "_links": [
        {
        "rel": "closePHR",
        "href": "http://localhost:8080/rest/phr/2/close",
        "method": "POST"
        }
        ]
        }

## PHR Update Operations [/rest/phr/id/update]
### Plant Hire Request Update [POST]

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
            "name": "Vinod",
            "description": "Vinod",
            "price": 400,
            "links": [
        {
            "rel": "self",
            "href": "http://localhost:8070/rest/plants/1"
        }
        ]
        }

