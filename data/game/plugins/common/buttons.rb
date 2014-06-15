# The logout button mapping
LOGOUT_BUTTON            = { :id => 182, :component => 6 }

# The graphics setting button mapping
GRAPHICS_SETTINGS_BUTTON = { :id => 261, :component => 16 }

# The running toggle button mapping
RUN_SETTINGS_BUTTON      = { :id => 261, :component => 3 }

# The running orb toggle mapping
RUN_ORB_TOGGLE           = { :id => 750, :component => 1 }

# The chat effects toggle button mapping
CHAT_EFFECTS_SETTINGS_BUTTON  = { :id => 261, :component => 4 }

# The split private chat toggle button mapping
PCHAT_SPLIT_SETTINGS_BUTTON = { :id => 261, :component => 5 }

# The split mouse toggle button mapping
SPLIT_MOUSE_SETTINGS_BUTTON  = { :id => 261, :component => 6 }

# The accept aid toggle button mapping
ACCEPT_AID_SETTINGS_BUTTON  = { :id => 261, :component => 7 }

# Bind the logout button
bind :btn, LOGOUT_BUTTON do
  player.logout
end

bind :btn, GRAPHICS_SETTINGS_BUTTON do
  player.interface_set.open_window(742)
end

bind :btn, RUN_SETTINGS_BUTTON do
  player.settings.toggle_running
end

bind :btn, RUN_ORB_TOGGLE do
  player.settings.toggle_running
end

bind :btn, CHAT_EFFECTS_SETTINGS_BUTTON do
  player.settings.toggle_chat_fancy
end

bind :btn, PCHAT_SPLIT_SETTINGS_BUTTON do
  player.settings.toggle_private_chat_split
end

bind :btn, SPLIT_MOUSE_SETTINGS_BUTTON do
  player.settings.toggle_two_button_mouse
end

bind :btn, ACCEPT_AID_SETTINGS_BUTTON do
  player.settings.toggle_accepting_aid
end
