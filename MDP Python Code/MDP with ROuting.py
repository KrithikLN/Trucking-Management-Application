import mysql.connector
import folium
import time
import requests
import webbrowser

# Connect to MySQL database
try:
    conn = mysql.connector.connect(
        host="localhost",
        user="root",
        password="2004",
        database="ValidationDB"
    )
    print("Successfully connected to the database!")
except mysql.connector.Error as e:
    print(f"Error connecting to the database: {e}")

if conn:
    # Create a cursor object to execute SQL queries
    cursor = conn.cursor()

    # Initialize map centered at the initial location
    truck_map = folium.Map(location=[13.0850, 80.2101], zoom_start=10)
    html_file = 'truck_map.html'  # File to save the map

    # Open the HTML file in the default web browser
    webbrowser.open(html_file, new=2)

    while True:
        # Fetch current truck location from the database
        cursor.execute(
            "SELECT latitude, longitude FROM location_entity WHERE device_id = '69ad665ca0c8353c'"
        )
        current_location = cursor.fetchone()
        if current_location:
            print("Current Location:", current_location)

            # Add marker for current truck location
            folium.Marker(current_location, popup='Truck Location', icon=folium.Icon(color='blue')).add_to(
                truck_map)

            # Get route from current location to destination using OSRM API
            destination_coordinates = (12.7378, 80.1753)  # Replace with your destination coordinates
            url = f"http://router.project-osrm.org/route/v1/driving/{current_location[1]},{current_location[0]};{destination_coordinates[1]},{destination_coordinates[0]}?geometries=geojson"
            response = requests.get(url)
            if response.status_code == 200:
                data = response.json()
                route_coordinates = data['routes'][0]['geometry']['coordinates']
                # Reverse the order of latitude and longitude for each coordinate
                route_coordinates_reversed = [(coord[1], coord[0]) for coord in route_coordinates]

                # Draw the route line
                route_line = folium.PolyLine(locations=route_coordinates_reversed, color='green')
                route_line.add_to(truck_map)

                print("Route found:", route_coordinates_reversed)
            else:
                print("No route found.")

            # Save map to HTML file
            truck_map.save(html_file)

        # Wait for 5 seconds before checking again
        time.sleep(5)
