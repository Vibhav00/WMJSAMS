# Android Task Scheduling Demo

This project demonstrates how to efficiently schedule background tasks in Android using **WorkManager**, **JobScheduler**, and **AlarmManager**.

## Overview
- **WorkManager**: Best for tasks that require constraints (e.g., network availability, battery status). Ensures execution even after app restarts.
- **JobScheduler**: Suitable for short periodic tasks that don't require immediate execution.
- **AlarmManager**: Ideal for scheduling precise tasks at a specific time, even when the app is closed.

## Features
- Background task execution with constraints
- Periodic and one-time task scheduling
- Ensures task execution even after device reboot

## License
MIT License – Free to use and modify.
