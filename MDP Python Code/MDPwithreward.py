import numpy as np

# Define state space including additional states
states = ['Chennai', 'Madurai', 'Coimbatore', 'Loading', 'Unloading', 'En route to Destination', 'Waiting', 'Breakdown', 'Refueling', 'Traffic Congestion', 'Detour', 'Weather Conditions']
drivers = ['Arjun', 'Vikram', 'Meena', 'Anjali', 'Ramesh', 'Deepak']
actions = ['Drive', 'Stop', 'Load', 'Unload', 'Repair', 'Refuel', 'Wait']

# Initialize Q-table with zeros
Q = np.zeros((len(states), len(drivers), len(actions)))

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
    next_action_index = min(len(actions) - 1, choose_action(next_state, driver))  # Ensure next action index is within valid range
    Q[state, driver, action] += alpha * (reward + gamma * Q[next_state, driver, next_action_index] - Q[state, driver, action])


# Reward function
def calculate_reward(state, action):
    # Define rewards for different state-action pairs
    rewards = {
        ('Chennai', 'Drive'): 5,
        ('Madurai', 'Drive'): 4,
        ('Coimbatore', 'Drive'): 3,
        ('Loading', 'Load'): 2,
        ('Unloading', 'Unload'): 2,
        ('En route to Destination', 'Drive'): 5,
        ('Waiting', 'Wait'): 1,
        ('Breakdown', 'Repair'): -10,  # Penalty for breakdown
        ('Refueling', 'Refuel'): 3,
        ('Traffic Congestion', 'Drive'): -3,
        ('Detour', 'Detour'): -5,      # Penalty for detour
        ('Weather Conditions', 'Drive'): -2
    }

    # Retrieve reward for the given state-action pair
    reward = rewards.get((state, action), 0)  # Default reward is 0 if not defined in the dictionary

    return reward



# Function to simulate the model using the learned policy
def simulate_model(num_episodes=100):
    total_rewards = 0
    episode_details = []  # List to store details of each episode

    for _ in range(num_episodes):
        episode_reward = 0
        episode_steps = []  # List to store details of each step in the episode
        state = np.random.choice(len(states))  # Start from a random state
        driver = np.random.choice(len(drivers))  # Assign a random driver

        while True:
            action = choose_action(state, driver)  # Choose action using learned policy
            reward = calculate_reward(states[state], actions[action])  # Calculate reward based on current state-action pair

            episode_reward += reward
            next_state = np.random.choice(len(states))  # Transition to a random next state

            # Store episode step details
            episode_steps.append({
                'state': states[state],
                'driver': drivers[driver],
                'action': actions[action],
                'reward': reward,
                'next_state': states[next_state]
            })

            if action == actions.index('Stop'):  # If stop action, break the loop
                break

            state = next_state

        total_rewards += episode_reward
        episode_details.append({
            'total_reward': episode_reward,
            'steps': episode_steps
        })

    avg_reward = total_rewards / num_episodes
    return avg_reward, episode_details


# Training the Q-learning algorithm
num_episodes = 1000
for episode in range(num_episodes):
    state = np.random.choice(len(states))  # Start from a random state
    driver = np.random.choice(len(drivers))  # Assign a random driver

    while True:
        action = choose_action(state, driver)
        reward = calculate_reward(states[state], actions[action])  # Calculate reward based on current state-action pair
        next_state = np.random.choice(len(states))  # Transition to a random next state
        update_Q(state, driver, action, reward, next_state)

        if action == actions.index('Stop'):  # If stop action, break the loop
            break

        state = next_state

# Simulate the model
avg_reward, episode_details = simulate_model(num_episodes=1000)

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
