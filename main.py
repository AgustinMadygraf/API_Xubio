import os
import requests
from requests.auth import HTTPBasicAuth
from dotenv import load_dotenv

# Cargar las variables del archivo .env
load_dotenv()

# Obtener credenciales desde el archivo .env
client_id = os.getenv('CLIENT_ID')
secret_id = os.getenv('SECRET_ID')

if not client_id or not secret_id:
    print("Error: CLIENT_ID o SECRET_ID no encontrados en el archivo .env")
    exit()

# URL para obtener el token de acceso
token_url = 'https://xubio.com/API/1.1/TokenEndpoint'

# Datos para la solicitud del token
data = {
    'grant_type': 'client_credentials'
}

# Solicitud POST para obtener el token de acceso
response = requests.post(token_url, data=data, auth=HTTPBasicAuth(client_id, secret_id))

# Verifica si la solicitud fue exitosa
if response.status_code == 200:
    # Extrae el token de acceso del JSON de respuesta
    access_token = response.json().get('access_token')
    print('Access Token obtenido:', access_token)

    # Define el encabezado de autorización con el token obtenido
    headers = {
        'Authorization': f'Bearer {access_token}',
        'Accept': 'application/json'
    }

    # URL del recurso al que deseas acceder
    api_url = 'https://xubio.com/API/1.1/clienteBean'

    # Solicitud GET al recurso de la API
    api_response = requests.get(api_url, headers=headers)

    # Verifica si la solicitud fue exitosa
    if api_response.status_code == 200:
        # Procesa la respuesta JSON
        clientes = api_response.json()
        print('Datos de clientes:', clientes)
    else:
        print('Error al acceder al recurso:', api_response.status_code, api_response.text)
else:
    print('Error al obtener el token de acceso:', response.status_code, response.text)
