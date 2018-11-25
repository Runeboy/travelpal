from flask import Flask, Response, json, request
app = Flask(__name__)

from core import * 


@app.route('/')
def ahello():
    """Return a friendly HTTP greeting."""
    return 'TravelPal v1.0!'

@app.route('/getPriceQuote', methods=['GET'])
def getPriceQuote():
    fromCity = getRequestParam('from')
    toCity = getRequestParam('to')
    cost = getPrice(fromCity, toCity) #sum(ord(c) for c in (fromStation + toStation))
    return getJsonResponse({
        'from': fromCity,
        'to': toCity,
        'cost': str(cost)
        })

@app.route('/getClientUrl', methods=['GET'])
def server_getClientUrl():
    return getJsonResponse(getClientUrl(
        getRequestParam('clientId'),
        getRequestParam('key')      
        ))
    
@app.route('/getNewClientId', methods=['GET'])
def server_getNewClientId():
    return getJsonResponse({
        'clientId' : getNewClientId()
        })
    
@app.route('/ping', methods=['GET'])
def ping():  #(provider_name):
    return getJsonResponse('pong')		
    

@app.route('/getUserStatus', methods=['GET'])
def getUserStatus():  #(provider_name):
	#global isCheckedIn
	userId = getRequestParam('userId')
	return getJsonResponse({
		'isCheckedIn': (checkInCity != None),
		'from': checkInCity,
		'checkInTime': str(checkInTime)
		})		
    

@app.route('/checkIn', methods=['GET'])
def checkIn():  #(provider_name):
	global checkInCity
	global checkInTime	
	userId = getRequestParam('userId')
	checkInCity = getRequestParam('from')
	checkInTime = datetime.datetime.now()
	return getJsonResponse({
		'isCheckedIn': (checkInCity != None),
		'from': checkInCity,
		'checkInTime': str(checkInTime)
		})		
    

@app.route('/checkOut', methods=['GET'])
def checkOut():  #(provider_name):
	global checkInCity
	global checkInTime	
	if (checkInCity == None):
	 	raise Exception('Cannot checkout as user is not currently checked in.')
	userId = getRequestParam('userId')
	checkOutCity = getRequestParam('to')
	cost = getPrice(checkInCity, checkOutCity) #sum(ord(c) for c in (fromStation + toStation))
	response = getJsonResponse({
		'from': checkInCity,
		'to': checkOutCity,
		'cost': cost #
		})
	checkInCity = None
	checkInTime = None
	return response
    
@app.route('/prices', methods=['GET'])
def login():  #(provider_name):
    return getJsonResponse({
        'commontravels':[
			{	'stations':['San Francisco','Los Angeles'],
				'price':300
			},
			{	'stations':['Santa Cruz','San Francisco'],
				'price':200
			},
			{	'stations':['Bonny Doon','Santa Cruz'],
				'price':50
			},
			{	'stations':['Santa Cruz','Davis'],
				'price':450
			}
			],
			'defaultprice':20
    	})

@app.errorhandler(404)
def page_not_found(e):
    """Return a custom 404 error."""
    return 'Sorry, Nothing at this URL.', 404


@app.errorhandler(500)
def application_error(e):
    """Return a custom 500 error."""
    return 'Sorry, unexpected error: {}'.format(e), 500
