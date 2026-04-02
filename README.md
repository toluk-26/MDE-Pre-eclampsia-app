hello every one! yes, im lazy chud. the app is very behind

# TODO
 - [ ] move to menu page only on successful connect
 - [ ] going back to connect screen disconnects device
## implementations for later
- `implementation 'no.nordicsemi.android:dfu:2.11.0'`
  - this is the [nordic dfu library](https://github.com/NordicSemiconductor/Android-DFU-Library)
- configuration page
    - 1. begin test
    - 2. live data to watch PI. respond with generic sys/dia(rest, exercise, recovery). input 
    - 3. show result. must confirm
    - graph for systolic/diastolic over PI
    - send y=mx+b for each diastolic and systolic to firmware as m and b to store 

# services
- config
  - pid
  - min diastolic
  - max diastolic
  - min systolic
  - max systolic
  - diastolic coefficient m
  - diastolic coefficient b
  - systolic coefficient m
  - systolic coefficient b
  - new patient ready flag
  - demo mode
- time
  - unix time
  - timezone
- battery
  - battery level
- device info
  - model
  - serial
  - firmware
  - software
  - serial
  - manufacturer
- Data Transfer
  - stream
  - size
- calibrate
  - stream