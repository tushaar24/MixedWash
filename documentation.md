# Mixed Wash - Laundry Service App

## Architectural Business Logic Flow

This document details the core business logic flow of the Mixed Wash laundry service application, from user onboarding to service completion.

### 1. Authentication & Onboarding Flow

#### 1.1 User Onboarding and Authentication

When a new user launches the application:

1. **Initial Onboarding**:
   - The user is presented with an interactive onboarding carousel that explains key features of the Mixed Wash app
   - Onboarding data is loaded from a local JSON resource file (`files/mock/onboarding_data.json`)
   - The user can navigate through screens or skip ahead
   - At the final screen, they choose either "Explore" to start using the app or "Help Center" to view FAQs

2. **Sign In Process**:
   - After onboarding, the user is directed to the sign-in screen
   - Authentication is handled by the FirebaseUserService, which implements the UserService interface
   - User can authenticate using supported authentication methods (email/password, social logins)

3. **Phone Verification**:
   - Once the user signs in, the system checks if a phone number is registered
   - If no phone number exists, the user is prompted to enter one
   - The system sends a verification code to the provided number
   - User enters the verification code for validation
   - Upon successful verification, the phone number is added to the user's metadata in Firestore

4. **User Metadata Creation**:
   - When a new user completes authentication, the system creates a UserMetadata entry in Firestore
   - The UserMetadata contains essential user information including verified phone number and address information
   - The metadata is linked to the user's Firebase Authentication ID

#### 1.2 Authentication State Management

The authentication state is managed using a dedicated state model:

- **AuthState**: Represents different states of authentication:
  - **Loading**: Initial state or during authentication operations
  - **Authenticated**: User is successfully logged in
  - **Authenticating**: Authentication is in progress
  - **Unauthenticated**: User is not logged in

The authentication state is observed throughout the application to control access to protected features and ensure a consistent user experience.

### 2. Home Screen & Data Flow

#### 2.1 Home Screen Initialization

Once authenticated with a verified phone number:

1. **Home Screen Loading**:
   - The user is navigated to the home screen (`HomeScreen`)
   - The `HomeViewModel` initializes with dependency injection via Koin
   - The ViewModel follows the MVVM pattern with MVI concepts for state management

2. **Data Retrieval Process**:
   - The `HomeViewModel` calls `HomeRepository` to fetch data in parallel:
     - Promotions data: Special offers and banners
     - Service recommendations: Based on user history or default recommendations for new users
     - Active orders: If the user has any ongoing service requests

3. **Data Source**:
   - Currently, the home screen data is fetched from local JSON resources
   - The data structure includes:
     - **Promotions**: List of promotional banners with images, descriptions, and action links
     - **Service Categories**: Available service types with icons and descriptions
     - **Recommended Services**: Personalized recommendations based on usage history
     - **Active Orders**: Current order status and information

4. **State Management**:
   - Data is exposed via `StateFlow<HomeScreenState>` from the ViewModel
   - UI components observe this state and render accordingly
   - Side effects like navigation are handled through a channel of `HomeScreenUiEvent`

#### 2.2 User Interface Components

The home screen consists of several key components:

1. **Carousel**: Auto-scrolling promotional banner display with custom animations
2. **ServiceCards**: Grid display of service categories with icons
3. **RecommendedServices**: Horizontal list of personalized service recommendations
4. **ActiveOrderStatus**: Status display for any in-progress orders

### 3. Location Selection & Serviceability

#### 3.1 Location Serviceability Check

A critical business logic flow is determining if services are available at a user's location:

1. **Location Selection Methods**:
   - User can select a saved address from their profile
   - User can add a new address through the AddressForm component
   - User can search for an address using the AddressSearch component, which utilizes the Loki library for place search and autocomplete

2. **Address Validation Process**:
   - When an address is selected or entered:
     - The system extracts coordinates and/or pin code from the address
     - The `LocationAvailabilityRepository` is called to check serviceability
     - This repository delegates to the `LocationAvailabilityService` implementation

3. **Service Area Verification**:
   - The `FirebaseLocationAvailabilityService` queries Firestore for service area definitions
   - It checks if the selected location falls within any defined service area boundary
   - Verification can be performed using:
     - Coordinate-based matching: Checking if coordinates fall within service area polygons
     - Pin code-based matching: Checking if pin code is in the list of serviceable pin codes

4. **Result Handling**:
   - If the location is serviceable, the address is set as the current delivery address
   - If not serviceable, the user is notified that services are not available in their area
   - The system may suggest nearby serviceable areas if available

#### 3.2 Address Management

Address information is managed via the Address feature:

1. **Address Data Model**:
   - The `Address` domain model contains:
     - Title (e.g., "Home", "Office")
     - Address lines (street, building, etc.)
     - Pin code
     - Coordinates (latitude and longitude)

2. **Storage and Retrieval**:
   - Addresses are stored in the user's metadata in Firestore
   - The `FirebaseAddressRepositoryImpl` handles CRUD operations
   - Users can maintain multiple addresses and set one as the current delivery address

3. **Integration with User Profile**:
   - Address management is integrated with the user profile feature
   - Changes to addresses are reflected in real-time across the application

### 4. Key Business Logic Interactions

The flow between these components creates the core user experience:

1. User completes authentication and phone verification
2. Home screen displays personalized content and service options
3. User selects a service category and is navigated to service selection
4. User selects or confirms their delivery address
5. System verifies service availability at that location
6. If the location is serviceable, the user can proceed with booking
7. If not serviceable, the user is prompted to select a different address

This architecture ensures a smooth, seamless experience while maintaining clean separation of concerns between features.