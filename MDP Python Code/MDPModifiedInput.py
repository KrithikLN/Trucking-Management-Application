import numpy as np

# Define state space including additional states and sheds
sheds = ['Chennai', 'Madurai', 'Coimbatore']
additional_states = ['Loading', 'Unloading', 'En route to Destination', 'Waiting', 'Breakdown', 'Refueling',
                     'Traffic Congestion', 'Detour', 'Weather Conditions']
drivers = ['Sasi', 'Pandian', 'Kumar', 'Raju', 'Bharathi', 'Sathish']
actions = ['Drive', 'Stop', 'Load', 'Unload', 'Repair', 'Refuel', 'Wait']

# Initialize Q-table with zeros
num_sheds = len(sheds)
Q = np.zeros((num_sheds + len(additional_states), len(drivers), len(actions)))

# Hyperparameters
alpha = 0.1  # Learning rate
gamma = 0.9  # Discount factor
epsilon = 0.1  # Exploration-exploitation trade-off


# Function to choose action using epsilon-greedy policy
def choose_action(state, driver):
    q_values = Q[state, driver]
    if np.random.uniform(0, 1) < epsilon:
        return np.random.choice(len(actions))
    else:
        return np.argmax(q_values)


# Function to update Q-values using Q-learning update rule
def update_Q(state, driver, action, reward, next_state):
    next_action_index = min(len(actions) - 1,
                            choose_action(next_state, driver))  # Ensure next action index is within valid range
    Q[state, driver, action] += alpha * (
                reward + gamma * Q[next_state, driver, next_action_index] - Q[state, driver, action])


# Reward function
def calculate_reward(state, action):
    rewards = {
        ('Drive', 'Chennai'): 5,
        ('Drive', 'Madurai'): 4,
        ('Drive', 'Coimbatore'): 3,
        ('Load', 'Loading'): 2,
        ('Unload', 'Unloading'): 2,
        ('Drive', 'En route to Destination'): 5,
        ('Wait', 'Waiting'): 1,
        ('Repair', 'Breakdown'): -10,  # Penalty for breakdown
        ('Refuel', 'Refueling'): 3,
        ('Drive', 'Traffic Congestion'): -3,
        ('Detour', 'Detour'): -5,  # Penalty for detour
        ('Drive', 'Weather Conditions'): -2
    }

    # Retrieve reward for the given state-action pair
    reward = rewards.get((action, state), 0)  # Default reward is 0 if not defined in the dictionary

    return reward


# Function to simulate the model using the learned policy
def simulate_model(starting_shed_index, num_episodes=100):
    total_rewards = 0
    episode_details = []  # List to store details of each episode

    for _ in range(num_episodes):
        episode_reward = 0
        episode_steps = []  # List to store details of each step in the episode
        state = starting_shed_index  # Truck starts from the specified shed
        driver = np.random.choice(len(drivers))  # Assign a random driver

        while True:
            action = int(input(f"Enter the index of the action (0 to {len(actions) - 1}): "))  # Manually select action
            reward = calculate_reward(state, actions[action])  # Calculate reward based on current state-action pair

            episode_reward += reward

            next_state = np.argmax(Q[state, driver])  # Transition to the next state based on Q-values

            # Store episode step details
            episode_steps.append({
                'state': sheds[state] if state < num_sheds else additional_states[state - num_sheds],
                'driver': drivers[driver],
                'action': actions[action],
                'reward': reward,
                'next_state': additional_states[next_state - num_sheds] if next_state >= num_sheds else sheds[state]
            })

            if next_state == starting_shed_index:  # If returned to starting shed, break the loop
                break

            state = next_state

        total_rewards += episode_reward
        episode_details.append({
            'total_reward': episode_reward,
            'steps': episode_steps
        })

    avg_reward = total_rewards / num_episodes
    return avg_reward, episode_details


# Prompt the user to specify the starting shed
while True:
    try:
        print("Available sheds:")
        for i, shed in enumerate(sheds):
            print(f"{i}: {shed}")
        starting_shed_index = int(input(f"Enter the index of the starting shed (0 to {num_sheds - 1}): "))
        if 0 <= starting_shed_index < num_sheds:
            break
        else:
            print(f"Invalid input. Please enter a number between 0 and {num_sheds - 1}.")
    except ValueError:
        print("Invalid input. Please enter a valid integer.")

# Training the Q-learning algorithm
num_episodes = 1000
for episode in range(num_episodes):
    state = starting_shed_index  # Start from the specified shed
    driver = np.random.choice(len(drivers))  # Assign a random driver

    while True:
        action = int(input(f"Enter the index of the action (0 to {len(actions) - 1}): "))  # Manually select action
        reward = calculate_reward(state, actions[action])  # Calculate reward based on current state-action pair

        next_state = np.argmax(Q[state, driver])  # Transition to the next state based on Q-values
        update_Q(state, driver, action, reward, next_state)

        if next_state == starting_shed_index:  # If returned to starting shed, break the loop
            break

        state = next_state

# Simulate the model
avg_reward, episode_details = simulate_model(starting_shed_index, num_episodes=1000)

# Print average reward and episode details
print("Average reward:", avg_reward)
print("Episode details:")
for i, episode in enumerate(episode_details):
    print(f"Episode {i + 1}: Total Reward = {episode['total_reward']}")
    print("Steps:")
    for step in episode['steps']:
        print(
            f"  State: {step['state']}, Driver: {step['driver']}, Action: {step['action']}, Reward: {step['reward']}, Next State: {step['next_state']}")
    print()
