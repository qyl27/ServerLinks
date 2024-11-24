# ServerLinks
Show your server links in the client pause screen.  
It is the new features in 1.21, but not disclosed in vanilla.  

Now supported both Velocity and Spigot (and its forks).  

## Requirements
- Minecraft >= 1.21
- Spigot only
  - ProtocolLib >= 5.1.0

## Configurations

### Spigot
File name: `config.yml`

```yaml
server-links:
  # Client known types:
  # BUG_REPORT, COMMUNITY_GUIDELINES, SUPPORT, STATUS, FEEDBACK, COMMUNITY, WEBSITE, FORUMS, NEWS, ANNOUNCEMENTS
  - type: WEBSITE
    url: "https://github.com/MeowCraftMC/ServerLinks"
  # Custom type with text
  # Supports formatting codes (like &a) and hex colors (&#66ccff).
  - type: CUSTOM
    text: "&6Custom text"
    url: "https://www.example.com"
```

### Velocity
File name: `config.conf`

```hocon
server-links=[
    {
        type=WEBSITE
        url="https://github.com/MeowCraftMC/ServerLinks"
    },
    {
        label="&bCustom example.com"
        url="https://www.example.com"
    }
]
```

## Command & Permissions

### Spigot
Use `/serverlinks reload` to reload the config.

- `serverlinks.admin`: For admins who can reload the config.
- `serverlinks.user`: For users who can see these links.

### Velocity
Use `/velocity reload` to reload the config.  
No permissions provided.

## Screenshots
![photo1](https://github.com/MeowCraftMC/ServerLinks/raw/main/img/1.png)

## License
SSPL-1.0  
Copyright (c) 2024 qyl27  

```text
This program is free software: you can redistribute it and/or modify it under the terms of the Server Side Public License, version 1, as published by MongoDB, Inc. This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the Server Side Public License for more details. You should have received a copy of the Server Side Public License along with this program. If not, see <http://www.mongodb.com/licensing/server-side-public-license>.
```
