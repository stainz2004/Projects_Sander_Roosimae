import './App.css'
import {useEffect, useRef, useState} from "react";
import api from './services/api'
import axios from "axios";


function App() {
  const MAX_LOAN_AMOUNT = 10000;
  const MIN_LOAN_AMOUNT = 2000;
  const MIN_LOAN_PERIOD = 12;
  const MAX_LOAN_PERIOD = 60;
  const MIN_LOAN_PERIOD_STEP = 6;
  const MIN_LOAN_AMOUNT_STEP = 100;

  const [loanAmount, setLoanAmount] = useState<number>(2000)
  const [loanPeriod, setLoanPeriod] = useState<number>(12)
  const [personalCode, setPersonalCode] = useState<string>("")
  const personalCodeRef = useRef<string>("");

  const [decision, setDecision] = useState<LoanDecision | null>(null);
  const [error, setError] = useState<string>("");

  interface LoanDecision {
    loanAmount: number;
    loanPeriod: number;
  }

  const handlePersonalCodeChange = (value: string) => {
    setPersonalCode(value);
    personalCodeRef.current = value;

    if (!value.trim()) {
      setError("Please enter a personal code.");
      setDecision(null);
      return;
    }

    setError("");
  };

  // Made it so every time either one of the sliders is moved then it runs the backend request, personal code changes do not trigger it as that would be annoying to a customer.
  useEffect(() => {
    const timer = setTimeout(async () => {
      if (!personalCodeRef.current.trim()) {
        setDecision(null);
        setError("Please enter a personal code.");
        return;
      }

      try {
        setDecision(null);
        setError("");

        const response = await api.post("/loan/decision", {
          personalCode: personalCodeRef.current,
          loanAmount,
          loanPeriod,
        });

        setDecision(response.data);
        setError("");
      } catch (e) {
        setDecision(null);

        if (axios.isAxiosError(e)) {
          setError(e.response?.data?.message || e.message || "Failed to fetch decision");
        } else {
          setError("Failed to fetch decision");
        }
      }
    }, 300);

    return () => clearTimeout(timer);
  }, [loanAmount, loanPeriod]);

  return (
      // Tried to make the design looks similar to Inbanks design.
      <div className="search">
        <p>Personal code</p>
        <input className="personal-code-input" type="text" value={personalCode} onChange={event => handlePersonalCodeChange(event.target.value)}/>
        <p>Loan amount: {loanAmount}</p>
        <input
            type="range"
            min={MIN_LOAN_AMOUNT}
            max={MAX_LOAN_AMOUNT}
            step={MIN_LOAN_AMOUNT_STEP}
            value={loanAmount}
            onChange={event => setLoanAmount(Number(event.target.value))}
        />


        <p>Loan period: {loanPeriod}</p>
        <input
            type="range"
            min={MIN_LOAN_PERIOD}
            max={MAX_LOAN_PERIOD}
            step={MIN_LOAN_PERIOD_STEP}
            value={loanPeriod}
            onChange={event => setLoanPeriod(Number(event.target.value))}
        />
        {error && <p className="error-text">{error}</p>}
        {!error && decision && (
          <div className="decision-card">
            <p><strong>Approved amount:</strong> {decision.loanAmount}</p>
            <p><strong>Loan period:</strong> {decision.loanPeriod} months</p>
          </div>
        )}
      </div>
  )
}

export default App
