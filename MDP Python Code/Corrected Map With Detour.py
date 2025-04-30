import mysql.connector
import folium
import time
import requests
import webbrowser

def get_route(current_location):
    destination_coordinates = (12.7378, 80.1753)  # Replace with your destination coordinates
    url = f"http://router.project-osrm.org/route/v1/driving/{current_location[1]},{current_location[0]};{destination_coordinates[1]},{destination_coordinates[0]}?geometries=geojson"
    response = requests.get(url)
    if response.status_code == 200:
        data = response.json()
        route_coordinates = data['routes'][0]['geometry']['coordinates']
        # Reverse the order of latitude and longitude for each coordinate
        return [(coord[1], coord[0]) for coord in route_coordinates]
    else:
        return None

try:
    conn = mysql.connector.connect(
        host="localhost",
        user="root",
        password="2004",
        database="ValidationDB",
        autocommit=True,
        init_command="SET SESSION TRANSACTION ISOLATION LEVEL READ COMMITTED"
    )
    print("Successfully connected to the database!")

    cursor = conn.cursor()
    cursor.execute(
        "SELECT latitude, longitude FROM location_entity WHERE device_id = '69ad665ca0c8353c'"
    )
    initial_location = cursor.fetchone()
    if initial_location:
        print("Initial Location:", initial_location)

        truck_map = folium.Map(location=[initial_location[0], initial_location[1]], zoom_start=10)
        html_file = 'truck_map.html'

        webbrowser.open(html_file, new=2)

        while True:
            print("Fetching current location from the database...")
            cursor.execute(
                "SELECT latitude, longitude FROM location_entity WHERE device_id = '69ad665ca0c8353c'"
            )
            current_location = cursor.fetchone()
            print("Current Location (before processing):", current_location)
            if current_location:
                print("Current Location:", current_location)

                # Clear previous marker and route
                truck_map = folium.Map(location=[initial_location[0], initial_location[1]], zoom_start=10)
                folium.Marker(current_location, popup='Truck Location', icon=folium.Icon(color='blue')).add_to(truck_map)

                current_route = get_route(current_location)
                if current_route:
                    route_line = folium.PolyLine(locations=current_route, color='green')
                    route_line.add_to(truck_map)

                    print("Route found:", current_route)
                else:
                    print("No route found.")

                truck_map.save(html_file)
            else:
                print("Current location not found in the database.")

            print("Waiting for 5 seconds before fetching the current location again...")
            time.sleep(5)
    else:
        print("Initial location not found.")

except mysql.connector.Error as e:
    print(f"Error connecting to the database: {e}")
