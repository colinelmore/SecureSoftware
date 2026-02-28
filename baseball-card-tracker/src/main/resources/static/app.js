// ========================================
// BASEBALL CARD TRACKER - FRONTEND APP
// ========================================

// API Base URL
const API_URL = 'http://localhost:8080/api/cards';

// ========================================
// PAGE INITIALIZATION
// ========================================

// Load all cards when page loads
document.addEventListener('DOMContentLoaded', function() {
    loadAllCards();
});

// ========================================
// NAVIGATION
// ========================================

function showSection(sectionId) {
    // Hide all sections
    const sections = document.querySelectorAll('.content-section');
    sections.forEach(section => section.classList.remove('active'));
    
    // Show selected section
    const selectedSection = document.getElementById(sectionId);
    selectedSection.classList.add('active');
    
    // Update navigation buttons
    const navButtons = document.querySelectorAll('.nav-btn');
    navButtons.forEach(btn => btn.classList.remove('active'));
    event.target.classList.add('active');
    
    // Load data for the section
    if (sectionId === 'all-cards') {
        loadAllCards();
    }
}

// ========================================
// GET ALL CARDS
// ========================================

async function loadAllCards() {
    const cardsGrid = document.getElementById('cards-grid');
    const noCards = document.getElementById('no-cards');
    
    // Show loading
    cardsGrid.innerHTML = '<div class="loading">Loading cards...</div>';
    noCards.style.display = 'none';
    
    try {
        const response = await fetch(API_URL);
        
        if (!response.ok) {
            throw new Error('Failed to fetch cards');
        }
        
        const data = await response.json();
        
        // Check if data is an array and has cards
        if (Array.isArray(data) && data.length > 0) {
            displayCards(data);
        } else if (typeof data === 'string' && data.includes('No cards')) {
            // No cards found
            cardsGrid.innerHTML = '';
            noCards.style.display = 'block';
        } else {
            // Empty array
            cardsGrid.innerHTML = '';
            noCards.style.display = 'block';
        }
        
    } catch (error) {
        cardsGrid.innerHTML = `
            <div class="error-message">
                Error loading cards: ${error.message}
                <br><br>
                Make sure the service is running on http://localhost:8080
            </div>
        `;
    }
}

function displayCards(cards) {
    const cardsGrid = document.getElementById('cards-grid');
    
    cardsGrid.innerHTML = cards.map(card => `
        <div class="card">
            <div class="card-header">
                <div class="card-name">${card.firstName} ${card.lastName}</div>
                <div class="card-position">${card.position}</div>
            </div>
            <div class="card-body">
                <div class="card-row">
                    <span class="card-label">Team:</span>
                    <span class="card-value">${card.team}</span>
                </div>
                <div class="card-row">
                    <span class="card-label">Height:</span>
                    <span class="card-value">${card.height}"</span>
                </div>
                <div class="card-row">
                    <span class="card-label">Weight:</span>
                    <span class="card-value">${card.weight} lbs</span>
                </div>
                <div class="batting-average">
                    Batting Avg: ${card.battingAverage.toFixed(3)}
                </div>
            </div>
        </div>
    `).join('');
}

// ========================================
// CREATE CARD
// ========================================

async function createCard(event) {
    event.preventDefault();
    
    const resultDiv = document.getElementById('create-result');
    
    // Get form values
    const cardData = {
        firstName: document.getElementById('create-firstname').value.trim(),
        lastName: document.getElementById('create-lastname').value.trim(),
        height: parseFloat(document.getElementById('create-height').value),
        weight: parseFloat(document.getElementById('create-weight').value),
        position: document.getElementById('create-position').value.trim(),
        team: document.getElementById('create-team').value.trim(),
        battingAverage: parseFloat(document.getElementById('create-batting').value)
    };
    
    // Show loading
    resultDiv.innerHTML = '<div class="loading">Creating card...</div>';
    
    try {
        const response = await fetch(`${API_URL}/add`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(cardData)
        });
        
        const result = await response.text();
        
        if (response.ok && result.includes('Success')) {
            resultDiv.innerHTML = `
                <div class="success-message">
                    ✓ ${result}
                    <br><br>
                    The card has been added to the database!
                </div>
            `;
            
            // Reset form
            document.getElementById('create-form').reset();
            
            // Reload all cards if on that tab
            setTimeout(() => {
                loadAllCards();
            }, 1000);
            
        } else {
            resultDiv.innerHTML = `
                <div class="error-message">
                    ${result}
                </div>
            `;
        }
        
    } catch (error) {
        resultDiv.innerHTML = `
            <div class="error-message">
                Error: ${error.message}
            </div>
        `;
    }
}

function resetCreateForm() {
    document.getElementById('create-form').reset();
    document.getElementById('create-result').innerHTML = '';
}

// ========================================
// EDIT CARD
// ========================================

async function loadCardForEdit() {
    const firstName = document.getElementById('edit-search-firstname').value.trim();
    const lastName = document.getElementById('edit-search-lastname').value.trim();
    const resultDiv = document.getElementById('edit-result');
    const formContainer = document.getElementById('edit-form-container');
    
    // Validate input
    if (!firstName || !lastName) {
        resultDiv.innerHTML = `
            <div class="error-message">
                Please enter both first name and last name.
            </div>
        `;
        formContainer.style.display = 'none';
        return;
    }
    
    // Show loading
    resultDiv.innerHTML = '<div class="loading">Loading card...</div>';
    formContainer.style.display = 'none';
    
    try {
        const response = await fetch(
            `${API_URL}/get?firstName=${encodeURIComponent(firstName)}&lastName=${encodeURIComponent(lastName)}`
        );
        
        const data = await response.json();
        
        if (response.ok && data.firstName) {
            // Card found - populate the edit form
            document.getElementById('edit-firstname').value = data.firstName;
            document.getElementById('edit-lastname').value = data.lastName;
            document.getElementById('edit-height').value = data.height;
            document.getElementById('edit-weight').value = data.weight;
            document.getElementById('edit-position').value = data.position;
            document.getElementById('edit-team').value = data.team;
            document.getElementById('edit-batting').value = data.battingAverage;
            
            // Show the edit form
            formContainer.style.display = 'block';
            resultDiv.innerHTML = `
                <div class="success-message">
                    Card loaded! Update the fields below and click "Update Card".
                </div>
            `;
            
        } else {
            // Card not found
            resultDiv.innerHTML = `
                <div class="error-message">
                    Card not found for ${firstName} ${lastName}
                </div>
            `;
            formContainer.style.display = 'none';
        }
        
    } catch (error) {
        resultDiv.innerHTML = `
            <div class="error-message">
                Error: ${error.message}
            </div>
        `;
        formContainer.style.display = 'none';
    }
}

async function updateCard(event) {
    event.preventDefault();
    
    const resultDiv = document.getElementById('edit-result');
    
    // Get form values
    const cardData = {
        firstName: document.getElementById('edit-firstname').value,
        lastName: document.getElementById('edit-lastname').value,
        height: parseFloat(document.getElementById('edit-height').value),
        weight: parseFloat(document.getElementById('edit-weight').value),
        position: document.getElementById('edit-position').value.trim(),
        team: document.getElementById('edit-team').value.trim(),
        battingAverage: parseFloat(document.getElementById('edit-batting').value)
    };
    
    // Show loading
    resultDiv.innerHTML = '<div class="loading">Updating card...</div>';
    
    try {
        const response = await fetch(`${API_URL}/update`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(cardData)
        });
        
        const result = await response.text();
        
        if (response.ok && result.includes('Success')) {
            resultDiv.innerHTML = `
                <div class="success-message">
                    ✓ ${result}
                    <br><br>
                    The card has been updated in the database!
                </div>
            `;
            
            // Hide edit form after successful update
            setTimeout(() => {
                document.getElementById('edit-form-container').style.display = 'none';
                document.getElementById('edit-search-firstname').value = '';
                document.getElementById('edit-search-lastname').value = '';
            }, 2000);
            
            // Reload all cards
            loadAllCards();
            
        } else {
            resultDiv.innerHTML = `
                <div class="error-message">
                    ${result}
                </div>
            `;
        }
        
    } catch (error) {
        resultDiv.innerHTML = `
            <div class="error-message">
                Error: ${error.message}
            </div>
        `;
    }
}

function cancelEdit() {
    document.getElementById('edit-form-container').style.display = 'none';
    document.getElementById('edit-search-firstname').value = '';
    document.getElementById('edit-search-lastname').value = '';
    document.getElementById('edit-result').innerHTML = '';
}

// ========================================
// SEARCH SINGLE CARD
// ========================================

async function searchCard() {
    const firstName = document.getElementById('search-firstname').value.trim();
    const lastName = document.getElementById('search-lastname').value.trim();
    const resultDiv = document.getElementById('search-result');
    
    // Validate input
    if (!firstName || !lastName) {
        resultDiv.innerHTML = `
            <div class="error-message">
                Please enter both first name and last name.
            </div>
        `;
        return;
    }
    
    // Show loading
    resultDiv.innerHTML = '<div class="loading">Searching...</div>';
    
    try {
        const response = await fetch(
            `${API_URL}/get?firstName=${encodeURIComponent(firstName)}&lastName=${encodeURIComponent(lastName)}`
        );
        
        const data = await response.json();
        
        if (response.ok && data.firstName) {
            // Card found
            resultDiv.innerHTML = `
                <div class="result-card">
                    <h3>${data.firstName} ${data.lastName}</h3>
                    <div class="result-row">
                        <span class="result-label">Position:</span>
                        <span>${data.position}</span>
                    </div>
                    <div class="result-row">
                        <span class="result-label">Team:</span>
                        <span>${data.team}</span>
                    </div>
                    <div class="result-row">
                        <span class="result-label">Height:</span>
                        <span>${data.height}"</span>
                    </div>
                    <div class="result-row">
                        <span class="result-label">Weight:</span>
                        <span>${data.weight} lbs</span>
                    </div>
                    <div class="result-row">
                        <span class="result-label">Batting Average:</span>
                        <span>${data.battingAverage.toFixed(3)}</span>
                    </div>
                </div>
            `;
        } else {
            // Card not found
            resultDiv.innerHTML = `
                <div class="error-message">
                    Card not found for ${firstName} ${lastName}
                </div>
            `;
        }
        
    } catch (error) {
        resultDiv.innerHTML = `
            <div class="error-message">
                Error: ${error.message}
            </div>
        `;
    }
}

// ========================================
// GET CARD STYLE
// ========================================

async function getCardStyle() {
    const styleId = document.getElementById('style-id').value.trim();
    const resultDiv = document.getElementById('style-result');
    
    // Validate input
    if (!styleId || isNaN(styleId)) {
        resultDiv.innerHTML = `
            <div class="error-message">
                Please enter a valid style ID (number).
            </div>
        `;
        return;
    }
    
    // Show loading
    resultDiv.innerHTML = '<div class="loading">Loading style...</div>';
    
    try {
        const response = await fetch(`${API_URL}/style/get?id=${styleId}`);
        const data = await response.json();
        
        if (response.ok && data.manufacturer) {
            // Style found
            resultDiv.innerHTML = `
                <div class="result-card">
                    <h3>Card Style #${styleId}</h3>
                    <div class="result-row">
                        <span class="result-label">Manufacturer:</span>
                        <span>${data.manufacturer}</span>
                    </div>
                    <div class="result-row">
                        <span class="result-label">Year:</span>
                        <span>${data.year}</span>
                    </div>
                    <div class="result-row">
                        <span class="result-label">Edition:</span>
                        <span>${data.edition}</span>
                    </div>
                </div>
            `;
        } else {
            // Style not found
            resultDiv.innerHTML = `
                <div class="error-message">
                    Card style not found with ID ${styleId}
                </div>
            `;
        }
        
    } catch (error) {
        resultDiv.innerHTML = `
            <div class="error-message">
                Error: ${error.message}
            </div>
        `;
    }
}

// ========================================
// GET FIND CARD ENTRY
// ========================================

async function getFindCard() {
    const firstName = document.getElementById('find-firstname').value.trim();
    const lastName = document.getElementById('find-lastname').value.trim();
    const resultDiv = document.getElementById('find-result');
    
    // Validate input
    if (!firstName || !lastName) {
        resultDiv.innerHTML = `
            <div class="error-message">
                Please enter both first name and last name.
            </div>
        `;
        return;
    }
    
    // Show loading
    resultDiv.innerHTML = '<div class="loading">Searching...</div>';
    
    try {
        const response = await fetch(
            `${API_URL}/find/get?firstName=${encodeURIComponent(firstName)}&lastName=${encodeURIComponent(lastName)}`
        );
        
        const data = await response.json();
        
        if (response.ok && data.firstName) {
            // Entry found
            resultDiv.innerHTML = `
                <div class="result-card">
                    <h3>Find Card Entry</h3>
                    <div class="result-row">
                        <span class="result-label">First Name:</span>
                        <span>${data.firstName}</span>
                    </div>
                    <div class="result-row">
                        <span class="result-label">Last Name:</span>
                        <span>${data.lastName}</span>
                    </div>
                    <div class="result-row">
                        <span class="result-label">Team Name:</span>
                        <span>${data.teamName}</span>
                    </div>
                </div>
            `;
        } else {
            // Entry not found
            resultDiv.innerHTML = `
                <div class="error-message">
                    Find card entry not found for ${firstName} ${lastName}
                </div>
            `;
        }
        
    } catch (error) {
        resultDiv.innerHTML = `
            <div class="error-message">
                Error: ${error.message}
            </div>
        `;
    }
}

// ========================================
// HELPER FUNCTIONS
// ========================================

// Allow Enter key to trigger search
document.addEventListener('keypress', function(event) {
    if (event.key === 'Enter') {
        const activeSection = document.querySelector('.content-section.active');
        
        if (activeSection.id === 'search-card') {
            searchCard();
        } else if (activeSection.id === 'card-styles') {
            getCardStyle();
        } else if (activeSection.id === 'find-cards') {
            getFindCard();
        }
    }
});