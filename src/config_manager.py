"""
Path: src/config_manager.py

"""

import os
import json
from datetime import datetime, timedelta

CONFIG_PATH = os.path.join('src', 'config.json')

def ensure_config_file():
    """Verifica si existe config.json; si no, lo crea con estructura inicial."""
    if not os.path.exists(CONFIG_PATH):
        data = {
            "access_token": "",
            "created_at": ""
        }
        with open(CONFIG_PATH, 'w', encoding='utf-8') as f:
            json.dump(data, f, indent=2)

def load_token_info():
    """Lee el token y la fecha de creación desde config.json."""
    with open(CONFIG_PATH, 'r', encoding='utf-8') as f:
        data = json.load(f)
        return data.get("access_token", ""), data.get("created_at", "")

def save_token_info(token, created_at):
    """Guarda el token y la fecha de creación en config.json de forma atómica."""
    temp_file = CONFIG_PATH + ".tmp"
    data = {
        "access_token": token,
        "created_at": created_at
    }
    with open(temp_file, 'w', encoding='utf-8') as f:
        json.dump(data, f, indent=2)
    # Renombrado atómico (evita inconsistencias si falla en medio de la escritura)
    os.replace(temp_file, CONFIG_PATH)

def is_token_valid(created_at_str):
    """Verifica si el token es válido (<1 hora)."""
    if not created_at_str:
        return False
    fmt = "%Y-%m-%d %H:%M:%S"
    created_at = datetime.strptime(created_at_str, fmt)
    now = datetime.now()
    return (now - created_at) < timedelta(hours=1)
