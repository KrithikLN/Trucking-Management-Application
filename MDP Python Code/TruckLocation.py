import mysql.connector
import folium
import time
import webbrowser
import tkinter as tk
from PIL import Image, ImageTk

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

    # Initialize Tkinter window
    root = tk.Tk()
    root.title("Truck Location")

    # Create a folium map centered at the initial location
    truck_map = folium.Map(location=[13.0850, 80.2101], zoom_start=10)

    # Initialize previous location
    prev_location = None

    # Function to update the map
    def update_map():
        global prev_location
        # Execute SQL query to retrieve current truck location
        cursor.execute(
            "SELECT latitude, longitude FROM location_entity WHERE device_id = '69ad665ca0c8353c'")  # Replace 'truck_id' with the actual device ID

        # Fetch the result
        result = cursor.fetchone()
        if result:
            latitude, longitude = result
            current_location = (latitude, longitude)
        else:
            current_location = None

        # Check if the current location is different from the previous one
        if current_location != prev_location:
            # Clear previous marker
            truck_map = folium.Map(location=current_location, zoom_start=10)

            # Add marker for current truck location
            folium.Marker(location=current_location, popup='Truck Location', icon=folium.Icon(color='blue')).add_to(
                truck_map)

            # Save the map to a temporary HTML file
            temp_html = 'temp_map.html'
            truck_map.save(temp_html)

            # Open the temporary HTML file in a web browser
            webbrowser.open(temp_html)

            # Update previous location
            prev_location = current_location

        # After 5 seconds, update the map again
        root.after(5000, update_map)

    # Run the function to update the map
    update_map()

    # Run the Tkinter event loop
    root.mainloop()
