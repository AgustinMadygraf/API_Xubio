from __future__ import annotations
from abc import ABC, abstractmethod
from src.services.client_data_processor import ClientDataProcessor 
from src.logs.config_logger import logger 



class Opcion(ABC):
    """
    The Strategy interface declares operations common to all supported versions
    of some algorithm.

    The Context uses this interface to call the algorithm defined by Concrete
    Strategies.
    """

    @abstractmethod
    def ejecutar(self, data: dict):
        pass


"""
Concrete Strategies implement the algorithm while following the base Strategy
interface. The interface makes them interchangeable in the Context.
"""


class OpcionObtenerDatosDeMiEmpresa(Opcion):
    def ejecutar(self, data: dict):
        try:
            company_data = data.api_client.get('miempresa')
            # Procesar y mostrar los datos de "mi empresa"
            if isinstance(company_data, dict):
                # Formatear la salida para "mi empresa"
                formatted_data = [company_data]
                ClientDataProcessor.process_data(formatted_data, data.is_json)
            else:
                logger.info("Formato inesperado de datos para 'mi empresa'.")
        except Exception as e:
            logger.info(f"Error al obtener los datos de 'mi empresa': {e}")


class OpcionObtenerListaDeCentroDeCostos(Opcion):
    def ejecutar(self, data: dict):
        return print("Centro de costos")

