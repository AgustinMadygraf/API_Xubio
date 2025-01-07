"""
Path: src/services/menu_service.py

"""

from src.services.client_data_processor import ClientDataProcessor
from src.logs.config_logger import logger

def run_main_menu(api_client, is_json):
    while True:
        print("\nOpciones:")
        print("0. Salir")
        print("1. Obtener lista de clientes")
        print("2. Obtener datos de mi empresa")
        print("3. Obtener lista de Centro de Costos")
        print("4. Obtener lista de Precios")
        print("5. Obtener lista de Vendedores")
        
        opcion = input("Selecciona una opción (0-5): ")

        if opcion == "1":
            try:
                client_data = api_client.get('clienteBean')
                ClientDataProcessor.process_data(client_data, is_json)
            except Exception as e:
                logger.info(f"Error al obtener la lista de clientes: {e}")
        elif opcion == "2":
            try:
                company_data = api_client.get('miempresa')
                # Procesar y mostrar los datos de "mi empresa"
                if isinstance(company_data, dict):
                    # Formatear la salida para "mi empresa"
                    formatted_data = [company_data]
                    ClientDataProcessor.process_data(formatted_data, is_json)
                else:
                    logger.info("Formato inesperado de datos para 'mi empresa'.")
            except Exception as e:
                logger.info(f"Error al obtener los datos de 'mi empresa': {e}")
        elif opcion == "3":
            try:
                cost_center_data = api_client.get('centroDeCostoBean')
                # Procesar y mostrar los datos de Centro de Costos
                if isinstance(cost_center_data, list):
                    ClientDataProcessor.process_data(cost_center_data, is_json)
                else:
                    logger.info("Formato inesperado de datos para 'centroDeCostoBean'.")
            except Exception as e:
                logger.info(f"Error al obtener la lista de Centros de Costos: {e}")
        elif opcion == "4":
            try:
                price_list_data = api_client.get('listaPrecioBean')
                # Procesar y mostrar los datos de Lista de Precios
                if isinstance(price_list_data, list):
                    ClientDataProcessor.process_data(price_list_data, is_json)
                else:
                    logger.info("Formato inesperado de datos para 'listaPrecioBean'.")
            except Exception as e:
                logger.info(f"Error al obtener la lista de Precios: {e}")
        elif opcion == "5":
            try:
                seller_data = api_client.get('vendedorBean')
                # Procesar y mostrar los datos de Vendedores
                if isinstance(seller_data, list):
                    ClientDataProcessor.process_data(seller_data, is_json)
                else:
                    logger.info("Formato inesperado de datos para 'vendedorBean'.")
            except Exception as e:
                logger.info(f"Error al obtener la lista de Vendedores: {e}")
        elif opcion == "0":
            logger.info("Saliendo del programa. ¡Hasta luego!")
            break
        else:
            logger.info("Opción inválida. Por favor, selecciona una opción válida.")
