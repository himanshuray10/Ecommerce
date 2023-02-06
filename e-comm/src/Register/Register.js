import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";

const Register = () => {

  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [name, setName] = useState("");

  const navigate = useNavigate();

  useEffect(() => {
    const auth = localStorage.getItem('user');
    if(auth){
      navigate('/')
    }
  })

  const handleRegister = async () => {
    let result = await fetch("http://localhost:5000/register", {
      method: "post",
      body: JSON.stringify({ name, email, password }),
      headers: {
        "content-Type": "application/json",
      },
    });
    result = await result.json();
    if (result.name) {
      localStorage.setItem("user", JSON.stringify(result));
      navigate("/");
    } else {
      alert("Please enter correct details");
    }
  };

  return (
    <div className="register">
      <h1>Register</h1>
      <input
        type={"text"}
        className="inputBox"
        placeholder="enter name"
        onChange={(e) => setName(e.target.value)}
        value={name}
      />
      <input
        type={"text"}
        className="inputBox"
        placeholder="enter email"
        onChange={(e) => setEmail(e.target.value)}
        value={email}
      />
      <input
        type={"password"}
        className="inputBox"
        placeholder="password"
        onChange={(e) => setPassword(e.target.value)}
        value={password}
      />
      <button className="appButton" onClick={() => handleRegister()}>
        Login
      </button>
    </div>
  );
};

export default Register;