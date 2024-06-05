from flask_restful import Resource, reqparse
from DBClass import User, UserSchema
import jwt
from flask import request
from FlaskAPP import app

class GetUserProfileInfo(Resource):
    def post(self):
        
        token = request.headers.get('Authorization')
        
        if not token:
            return {'message': 'Token is missing, Unauthorization'}, 401
        
        try:
            decoded = jwt.decode(token, app.config['SECRET_KEY'], algorithms=['HS256'])
            validUserID = decoded['id']
            validUserEmail = decoded['email']
            validDevice_info = decoded['device_info']
        except jwt.ExpiredSignatureError:
            return {'message': 'Token has expired'}, 401
        except jwt.InvalidTokenError:
            return {'message': 'Invalid token'}, 401
        
        request_device_info = request.headers.get('Device-Info')
        print("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!")
        print("device_info:", validDevice_info, "\nDevice-Info:", request_device_info)
        if validDevice_info != request_device_info:
            return {'message': 'invalid device'}, 401
        
        user = User.query.filter_by( id=validUserID ).first()
        
        if user is None: # 토큰으로 사용자를 DB에서 찾았을 때, 존재하지 않음
            return {'message': 'User not Found'}, 404
        
        elif user.email != validUserEmail:
            return {'message': 'User Email in Token not match in Server'}, 400
        
        else: # 토큰으로 사용자를 DB에서 찾았을 때, 존재함
            return {'message': UserSchema().dump(user)}, 200
        
        
        
        
        
        
        # if validUserPlace == 'admin': # 토큰으로 사용자를 DB에서 찾았을 때, 사용자의 계급이 어드민
        #         return {'message': {k: v for k, v in UserSchema().dump(user).items() if k not in {'passwd', 'email'}}}, 200
            
        # else: # 토큰으로 사용자를 DB에서 찾았을 때, 존재함
        #     if validUserPlace == 'admin': # 토큰으로 사용자를 DB에서 찾았을 때, 사용자의 계급이 어드민
        #         return {'message': {k: v for k, v in UserSchema().dump(user).items() if k not in {'passwd', 'email'}}}, 200
            
        #     else:
        #         return {'message': 'Permission denied'}, 403
    
        # parser = reqparse.RequestParser()
        # parser.add_argument('id', type=str, required=True, help='id must be string and necessary key')
        # args = parser.parse_args(strict=True)
        # myID = args['id']
        
        # if user.id == myID: # 이렇게 해야 하나??
        #     return {'message': UserSchema().dump(user)}, 200