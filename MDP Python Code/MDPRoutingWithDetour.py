import mysql.connector
import folium
import time
import requests
import webbrowser
import math

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

def detect_detour(initial_route, current_route):
    if initial_route and current_route:
        # Compare the initial and current routes
        if len(initial_route) != len(current_route):
            # If the lengths of the routes are different, a detour might have occurred
            return True
        else:
            for i in range(len(initial_route)):
                # Check if the distance between corresponding points exceeds a threshold
                initial_point = initial_route[i]
                current_point = current_route[i]
                distance = haversine_distance(initial_point, current_point)
                if distance > DETOUR_THRESHOLD:
                    return True
    return False

def haversine_distance(point1, point2):
    # Calculate the great-circle distance between two points on the Earth's surface
    # Using the Haversine formula
    lon1, lat1 = point1
    lon2, lat2 = point2
    R = 6371  # Radius of the Earth in kilometers
    dlat = math.radians(lat2 - lat1)
    dlon = math.radians(lon2 - lon1)
    a = math.sin(dlat / 2) ** 2 + math.cos(math.radians(lat1)) * math.cos(math.radians(lat2)) * math.sin(dlon / 2) ** 2
    c = 2 * math.atan2(math.sqrt(a), math.sqrt(1 - a))
    distance = R * c
    return distance

# Constants
DETOUR_THRESHOLD = 0.1 # Threshold distance in kilometers

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

        initial_route = get_route(initial_location)

        while True:
            print("Fetching current location from the database...")
            cursor.execute(
                "SELECT latitude, longitude FROM location_entity WHERE device_id = '69ad665ca0c8353c'"
            )
            current_location = cursor.fetchone()
            print("Current Location (before processing):", current_location)
            if current_location:
                print("Current Location:", current_location)

                folium.Marker(current_location, popup='Truck Location', icon=folium.Icon(color='blue')).add_to(
                    truck_map)

                current_route = get_route(current_location)
                if current_route:
                    if detect_detour(initial_route, current_route):
                        print("Detour detected!")
                        # Handle detour, such as updating the route on the map or sending a notification
                    else:
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