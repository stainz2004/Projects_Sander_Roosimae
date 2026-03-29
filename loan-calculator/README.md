# loan-approval-calculator

This is a simple loan approval calculator that takes into account various factors such as credit score, income, and loan amount to determine whether a loan application is approved or denied.

Frontend design was influenced by Inbanks loan calculator. Made it so inputting the personal code does not automatically trigger the loan approval process as that can be annoying for the user. Instead you need to change the fields for either loan amount/period to trigger the process.

The loan amount can only be incremented/decreased by 100 and the loan period can only be incremented/decreased by 6 months. This is to mock the process while in the real world the user could enter any amount.

I implemented a simple decision engine that takes in the users personal code, loan amount and loan period. Using that information the engine first tries to
find the person's credit score based on these rules:

Debt -> Personal code ends with 0 or 1.
Credit modifier 1 (100) -> Personal code ends with 2, 3.
Credit modifier 2 (300) -> Personal code ends with 4, 5, 6.
Credit modifier 3 (1000) -> Personal code ends with 7, 8, 9.

In doing so the engine does a mock personal code check to see if it is 11 characters long and if it ends with a number.

If the customer is not in debt and has a valid personal code then engine calculates the credit score and based on that it will start trying to find the maximum loan amount we would approve of.

The formula for calculating the maximum loan amount is as follows: credit modifier * loan period. The engine will start with the loan period provided by the user and if the loan is not approved then it will try to find another loan period that would be approved.

For the backend decision engine I also wrote some tests to make sure that the logic is working as expected. The tests cover various scenarios such as valid and invalid personal codes, different credit modifiers, and different loan periods.

## Technologies Used

- Java Spring Boot for the backend API (Java 25).
- React with TypeScript for the frontend user interface.

## Start up


1. Clone the repository to your local machine.
2. Navigate to the project directory.

Open a terminal and run the following commands:
```
cd loan-calculator
cd backend
./gradlew bootRun
```

Open another terminal
```
cd loan-calculator
cd frontend
npm install
npm run dev
```

If the project does not start correctly, then please message me the issue and I will fix it ASAP.

## Implemented Features

- Input fields for personal code, loan amount and loan period.
- Calculates the maximum loan amount based on the applicant's personal code, loan amount and loan period.
- Displays the result of the loan approval process.
- If no approved loan was found for the given information then the calculator will try to find another loan period that would be approved.
- If the person has bad credit (debt) then the decision engine will always return "Denied".
- Tests for the backend decision engine to ensure that the logic is working as expected.
