"""
Path: src/api_client.py

"""

import requests

class APIClient:
    """Responsable de interactuar con los endpoints de la API."""
    def __init__(self, base_url: str, access_token: str):
        self.base_url = base_url
        self.headers = {
            'Authorization': f'Bearer {access_token}',
            'Accept': 'application/json'
        }

    def get(self, endpoint: str) -> dict:
        """Realiza una solicitud GET a un endpoint específico."""
        url = f"{self.base_url}/{endpoint}"
        response = requests.get(url, headers=self.headers)

        if response.status_code != 200:
            raise ConnectionError(f"Error en GET {endpoint}: {response.status_code}, {response.text}")
        
        return response.json()
