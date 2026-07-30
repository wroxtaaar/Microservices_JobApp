const API_BASE_CANDIDATES = (() => {
  const currentHost = window.location.hostname;
  const protocol = window.location.protocol;
  const candidates = [];

  if (currentHost.includes('app.github.dev') || currentHost.includes('github.dev')) {
    candidates.push(`${protocol}//${currentHost.replace(/-\d+\./, '-8080.')}`);
  }

  if (currentHost === 'localhost' || currentHost === '127.0.0.1') {
    candidates.push('http://localhost:8080', 'http://localhost:8084');
  } else {
    candidates.push(`${protocol}//${currentHost}:8080`);
  }

  return candidates;
})();
let API_BASE = '';
const statusEl = document.getElementById('status');
const companiesListEl = document.getElementById('companiesList');
const jobsListEl = document.getElementById('jobsList');
const reviewsListEl = document.getElementById('reviewsList');
const reviewSummaryEl = document.getElementById('reviewSummary');
const companyCountEl = document.getElementById('companyCount');
const jobCountEl = document.getElementById('jobCount');
const reviewCountEl = document.getElementById('reviewCount');

const companySelects = [
  document.getElementById('jobCompanySelect'),
  document.getElementById('reviewCompanySelect')
];
const reviewCompanySelect = document.getElementById('companyReviewSelect');

let companies = [];
let jobs = [];
let reviews = [];

function setStatus(message, isError = false) {
  statusEl.textContent = message;
  statusEl.style.background = isError ? 'rgba(255, 90, 90, 0.16)' : 'rgba(76,201,240,0.1)';
  statusEl.style.borderColor = isError ? 'rgba(255,90,90,0.2)' : 'rgba(76,201,240,0.2)';
}

async function resolveApiBase() {
  if (API_BASE) {
    return API_BASE;
  }

  for (const candidate of API_BASE_CANDIDATES) {
    try {
      const response = await fetch(`${candidate}/companies`, { method: 'GET' });
      if (response.ok) {
        API_BASE = candidate;
        return candidate;
      }
    } catch {
      // Try the next candidate.
    }
  }

  API_BASE = API_BASE_CANDIDATES[0];
  return API_BASE;
}

async function fetchJson(url, options = {}) {
  const response = await fetch(url, {
    headers: { 'Content-Type': 'application/json' },
    ...options
  });

  const contentType = response.headers.get('content-type') || '';
  const body = contentType.includes('application/json') ? await response.json() : await response.text();

  if (!response.ok) {
    throw new Error(typeof body === 'string' ? body : JSON.stringify(body));
  }

  return body;
}

function renderCompanies() {
  companyCountEl.textContent = companies.length;
  if (!companies.length) {
    companiesListEl.innerHTML = '<div class="card"><p>No companies found yet.</p></div>';
    return;
  }

  companiesListEl.innerHTML = companies.map((company) => `
    <article class="card">
      <h3>${company.name}</h3>
      <p>${company.description || 'No description available.'}</p>
      <p>Rating: ${company.rating ?? 'N/A'}</p>
    </article>
  `).join('');
}

function renderJobs() {
  jobCountEl.textContent = jobs.length;
  if (!jobs.length) {
    jobsListEl.innerHTML = '<div class="card"><p>No jobs listed yet.</p></div>';
    return;
  }

  jobsListEl.innerHTML = jobs.map((job) => `
    <article class="card">
      <h3>${job.title}</h3>
      <p>${job.description || 'No description yet.'}</p>
      <p>${job.location || 'Remote'} • ${job.minSalary || '?'} - ${job.maxSalary || '?'}</p>
    </article>
  `).join('');
}

function renderCompanyOptions() {
  const options = companies.map((company) => `<option value="${company.id}">${company.name}</option>`).join('');
  companySelects.forEach((select) => {
    select.innerHTML = `<option value="">Select company</option>${options}`;
  });
  reviewCompanySelect.innerHTML = `<option value="">Select company</option>${options}`;
}

async function loadReviews(companyId = '') {
  if (!companyId) {
    reviewsListEl.innerHTML = '<div class="card"><p>Select a company to view reviews.</p></div>';
    reviewSummaryEl.innerHTML = '<p>No summary yet.</p>';
    return;
  }

  try {
    const base = await resolveApiBase();
    const data = await fetchJson(`${base}/reviews?companyId=${companyId}`);
    reviews = data;
    reviewCountEl.textContent = reviews.length;
    reviewSummaryEl.innerHTML = `<strong>${reviews.length}</strong> review(s) loaded for this company.`;
    if (!reviews.length) {
      reviewsListEl.innerHTML = '<div class="card"><p>No reviews yet for this company.</p></div>';
      return;
    }

    reviewsListEl.innerHTML = reviews.map((review) => `
      <article class="card">
        <h3>${review.title}</h3>
        <p>${review.description || 'No detail provided.'}</p>
        <p>Rating: ${review.rating}/5</p>
      </article>
    `).join('');
  } catch (error) {
    console.error(error);
    setStatus(`Could not load reviews: ${error.message}`, true);
  }
}

async function loadData() {
  try {
    setStatus('Loading platform data…');
    const base = await resolveApiBase();
    const [companiesData, jobsData] = await Promise.all([
      fetchJson(`${base}/companies`),
      fetchJson(`${base}/jobs`)
    ]);

    companies = companiesData;
    jobs = jobsData;
    renderCompanies();
    renderJobs();
    renderCompanyOptions();

    if (companies.length) {
      reviewCompanySelect.value = companies[0].id;
      await loadReviews(companies[0].id);
    } else {
      reviewCountEl.textContent = '0';
    }

    setStatus('Dashboard ready.');
  } catch (error) {
    setStatus(`Unable to reach the gateway API at ${API_BASE}. ${error.message}`, true);
    console.error(error);
  }
}

async function submitCompany(event) {
  event.preventDefault();
  const form = event.target;
  const payload = {
    name: form.name.value,
    description: form.description.value,
    rating: 0
  };

  try {
    const base = await resolveApiBase();
    await fetchJson(`${base}/companies`, {
      method: 'POST',
      body: JSON.stringify(payload)
    });
    form.reset();
    await loadData();
  } catch (error) {
    setStatus(`Company could not be created: ${error.message}`, true);
  }
}

async function submitJob(event) {
  event.preventDefault();
  const form = event.target;
  const payload = {
    title: form.title.value,
    description: form.description.value,
    minSalary: form.minSalary.value,
    maxSalary: form.maxSalary.value,
    location: form.location.value,
    companyId: Number(form.companyId.value)
  };

  try {
    const base = await resolveApiBase();
    await fetchJson(`${base}/jobs`, {
      method: 'POST',
      body: JSON.stringify(payload)
    });
    form.reset();
    await loadData();
  } catch (error) {
    setStatus(`Job could not be created: ${error.message}`, true);
  }
}

async function submitReview(event) {
  event.preventDefault();
  const form = event.target;
  const companyId = Number(form.companyId.value);
  const payload = {
    title: form.title.value,
    description: form.description.value,
    rating: Number(form.rating.value)
  };

  try {
    const base = await resolveApiBase();
    await fetchJson(`${base}/reviews?companyId=${companyId}`, {
      method: 'POST',
      body: JSON.stringify(payload)
    });
    form.reset();
    await loadReviews(companyId);
  } catch (error) {
    setStatus(`Review could not be created: ${error.message}`, true);
  }
}

function attachEvents() {
  document.getElementById('refreshBtn').addEventListener('click', loadData);
  document.getElementById('companyForm').addEventListener('submit', submitCompany);
  document.getElementById('jobForm').addEventListener('submit', submitJob);
  document.getElementById('reviewForm').addEventListener('submit', submitReview);
  reviewCompanySelect.addEventListener('change', (event) => loadReviews(event.target.value));
}

attachEvents();
loadData();
