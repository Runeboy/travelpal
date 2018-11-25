import urllib2
import time
import datetime
import uuid
from flask import Flask, Response, json, request


firebaseAppId = 'REMOVED'
checkInCity = None
checkInTime = None


def getJsonResponse(dict):
	return Response(response=json.dumps(dict),
    				status=200, \
    				mimetype="application/json")

def getRequestParam(name):
    str = request.args.get(name)
    assert str, 'parameter \'%s\' not specified' % name
    return str

def getClientUrl(clientId, key):
	return 'https://{0}.firebaseio.com/client/{1}/{2}.json'.format(firebaseAppId, clientId, key)

def getNewClientId():
	return str(uuid.uuid4())

def getPrice(fromCity, toCity):
	return sum(ord(c) for c in (fromCity + toCity))
