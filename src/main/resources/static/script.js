$(document).ready(function() {
    let userLabels = [];

    // Show login modal on page load
    $('#loginModal').modal('show');

    // Handle login form submission
    $('#login-form').on('submit', function (event) {
        event.preventDefault();
        
        const email = $('#email').val();
        const password = $('#password').val();
    
        console.log('Email:', email, 'Password:', password); // Kiểm tra log
    
        if (!email || !password) {
            alert('Vui lòng điền đầy đủ thông tin!');
            return;
        }
    
        $.ajax({
            url: 'http://localhost:8080/users/login',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({ email: email, password: password }),
            success: function (response) {
                const userName = response.name;
                const userId = response.id_user;
                $('#user-name').text(userName).data('userId', userId);
                $('#loginModal').modal('hide');
    
                // Fetch user-specific data
                fetchLabels(userId);
                fetchUserData(userId);
            },
            error: function (xhr) {
                console.error('Error:', xhr.responseText); // Log chi tiết lỗi
                alert('Login thất bại. Hãy kiểm tra lại thông tin bạn vừa nhập nhé!');
            }
        });
    });
    

    function resetForm() {
        $('#existing-label').val('');
        $('#new-label').val('');
        $('#so_tien').val('');
        $('#note').val('');
        $('#time_use').val('');
        $('#expense-form').data('edit-mode', false).data('expense-id', null); // Reset edit mode và expenseId
    }

    // Handle add expense button click
    $('#add-expense-btn').on('click', function () {
        resetForm(); // Đặt lại form trước khi mở modal
        populateLabelDropdown(); // Cập nhật danh sách nhãn
        $('#addExpenseModal').modal('show');
    });


    // Fetch existing labels for the logged-in user
    function fetchLabels(userId) {
        $.ajax({
            url: `http://localhost:8080/users/${userId}/labels`,
            type: 'GET',
            success: function(response) {
                userLabels = response;
                populateLabelDropdown();
            },
            error: function() {
                alert('Failed to fetch labels.');
            }
        });
    }


    // Populate label dropdown with stored labels
    function populateLabelDropdown() {
        const labelSelect = $('#existing-label');
        labelSelect.empty();
        labelSelect.append('<option value="">Select existing label</option>');
        userLabels.forEach(label => {
            labelSelect.append(`<option value="${label.id_label}">${label.name}</option>`);
        });
    }


    // Handle expense form submission
    $('#expense-form').on('submit', function (event) {
        event.preventDefault();
    
        const userId = $('#user-name').data('userId');
        const existingLabelId = $('#existing-label').val();
        const newLabelName = $('#new-label').val();
        const so_tien = $('#so_tien').val();
        const note = $('#note').val();
        const time_use = $('#time_use').val();
        const isEditMode = $(this).data('edit-mode');
        const expenseId = $(this).data('expense-id');
    
        if (isEditMode) {
            // Cập nhật expense
            const labelId = existingLabelId || newLabelName; // Dựa vào lựa chọn
            $.ajax({
                url: `http://localhost:8080/users/${userId}/labels/${labelId}/expenses/${expenseId}`,
                type: 'PUT',
                contentType: 'application/json',
                data: JSON.stringify({ so_tien, note, time_use }),
                success: function () {
                    alert('Expense updated successfully.');
                    $('#addExpenseModal').modal('hide');
                    resetForm(); // Reset form sau khi đóng modal
                    fetchUserData(userId); // Refresh danh sách dữ liệu
                },
                error: function () {
                    alert('Failed to update expense.');
                }
            });
        } else {
            // Logic thêm mới expense (như trước đây)
            if (existingLabelId) {
                addExpense(userId, existingLabelId, so_tien, note, time_use);
            } else if (newLabelName) {
                createLabelAndAddExpense(userId, newLabelName, so_tien, note, time_use);
            } else {
                alert('Please select an existing label or enter a new label.');
            }
        }
    });


    function addExpense(userId, labelId, so_tien, note, time_use) {
        $.ajax({
            url: `http://localhost:8080/users/${userId}/labels/${labelId}/expenses`,
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({ so_tien: so_tien, note: note, time_use: time_use }),
            success: function(response) {
                fetchUserData(userId); // Fetch lại dữ liệu mới nhất
                $('#addExpenseModal').on('hidden.bs.modal', function () {
                    resetForm();
                }); // Đóng modal
                $('#expense-form')[0].reset(); // Reset form
            },
            error: function() {
                alert('Failed to add expense.');
            }
        });
    }


    function createLabelAndAddExpense(userId, labelName, so_tien, note, time_use) {
        $.ajax({
            url: `http://localhost:8080/users/${userId}/labels`,
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({ name: labelName }),
            success: function(labelResponse) {
                addExpense(userId, labelResponse.id_label, so_tien, note, time_use);
                $('#expense-form')[0].reset(); // Reset form sau khi thêm mới
            },
            error: function() {
                alert('Failed to create label.');
            }
        });
    }


    function fetchUserData(userId) {
        $.ajax({
            url: `http://localhost:8080/users/${userId}/data`,
            type: 'GET',
            success: function(response) {
                displayUserData(response);
            },
            error: function() {
                alert('Failed to fetch user data.');
            }
        });
    }


    function displayUserData(user) {
        console.log('User data:', user); // Log toàn bộ dữ liệu trả về để kiểm tra
    
        $('#expenses-list').empty();
        if (user.labels && user.labels.length > 0) {
            user.labels.forEach(label => {
                label.expenses.forEach(expense => {
                    addExpenseToList(
                        label.name,
                        expense.so_tien,
                        expense.note,
                        expense.time_use,
                        expense.id_khoan, // Sử dụng id_khoan
                        label.id_label
                    );
                });
            });
        } else {
            console.log('No labels found for the user.');
        }
    }
    
    function addExpenseToList(labelName, so_tien, note, time_use, expenseId, labelId) {
        const expenseItem = `
            <div class="expense-item d-flex justify-content-between align-items-center" style="background-color: #15919b; color: white;">
                <div>
                    <h1 class="label-name" style="color: #a36361;">${labelName}</h1>
                    ${note ? `<p>${note}</p>` : ''}
                    <p>${time_use}</p>
                </div>
                <div class="d-flex align-items-center">
                    <h1>${so_tien}</h1>
                    <button class="btn btn-warning btn-sm mx-1 edit-expense-btn" 
                            data-expense-id="${expenseId}" 
                            data-label-id="${labelId}">
                        Edit
                    </button>
                    <button class="btn btn-danger btn-sm delete-expense-btn" 
                            data-expense-id="${expenseId}" 
                            data-label-id="${labelId}">
                        Delete
                    </button>
                </div>
            </div>
        `;
        $('#expenses-list').append(expenseItem);
    } 
    
    // Thử nghiệm
        // Nút delete 
        $('#expenses-list').on('click', '.delete-expense-btn', function () {
            const userId = $('#user-name').data('userId');
            const labelId = $(this).data('labelId');
            const expenseId = $(this).data('expenseId'); // Sẽ là id_khoan
        
            if (confirm('Are you sure you want to delete this expense?')) {
                $.ajax({
                    url: `http://localhost:8080/users/${userId}/labels/${labelId}/expenses/${expenseId}`,
                    type: 'DELETE',
                    success: function () {
                        alert('Expense deleted successfully.');
                        fetchUserData(userId); // Refresh data
                    },
                    error: function () {
                        alert('Failed to delete expense.');
                    }
                });
            }
        });
          
    
        // Nút edit
        $('#expenses-list').on('click', '.edit-expense-btn', function () {
            const userId = $('#user-name').data('userId');
            const labelId = $(this).data('labelId');
            const expenseId = $(this).data('expenseId'); // Sẽ là id_khoan
        
            // Mở modal và tải dữ liệu expense cần chỉnh sửa
            $.ajax({
                url: `http://localhost:8080/users/${userId}/labels/${labelId}/expenses/${expenseId}`,
                type: 'GET',
                success: function (expense) {
                    $('#existing-label').val(labelId); // Gán label
                    $('#new-label').val(''); // Clear new label
                    $('#so_tien').val(expense.so_tien);
                    $('#note').val(expense.note || '');
                    $('#time_use').val(expense.time_use);
        
                    // Lưu thông tin để cập nhật
                    $('#expense-form').data('edit-mode', true).data('expense-id', expenseId);
                    $('#addExpenseModal').modal('show');
                },
                error: function () {
                    alert('Failed to fetch expense data.');
                }
            });
        });

        
    // Mở modal khi nhấn nút "Xuất Báo Cáo"
    $('#export-report-btn').on('click', function () {
        $('#reportModal').modal('show');
    });

    // Xử lý form khi người dùng nhấn "Xem Báo Cáo"
    $('#report-form').on('submit', function (event) {
        event.preventDefault();

        const userId = 1; // Giả sử userId hiện tại là 1
        const month = $('#report-month').val();
        const year = $('#report-year').val();

        // Gửi yêu cầu tới backend để lấy báo cáo
        $.ajax({
            url: `http://localhost:8080/api/expenses/monthly-report?userId=${userId}&month=${month}&year=${year}`,
            method: 'GET',
            success: function (response) {
                // Hiển thị kết quả trong modal (thay thế nội dung cũ)
                $('#reportModal .modal-body').html(generateReportHTML(response));
            },
            error: function (error) {
                alert('Không thể lấy báo cáo. Vui lòng thử lại!');
                console.error(error);
            }
        });
    });

    // Hàm tạo HTML để hiển thị báo cáo
    function generateReportHTML(report) {
        if (!report || report.length === 0) {
            return '<p>Không có dữ liệu chi tiêu cho tháng này.</p>';
        }

        let html = '<h5>Báo Cáo Chi Tiêu</h5>';
        html += '<table class="table table-striped">';
        html += '<thead><tr><th>Danh Mục</th><th>Số Tiền</th></tr></thead>';
        html += '<tbody>';

        report.forEach(item => {
            html += `<tr>
                        <td>${item.labelName}</td>
                        <td>${item.totalSpent.toLocaleString()} VNĐ</td>
                    </tr>`;
        });

        html += '</tbody></table>';
        return html;
    } 

    // Mở pop up sign up
    // Mở modal đăng ký từ đăng nhập
    $('#open-signup').on('click', function (event) {
        event.preventDefault();
        $('#loginModal').modal('hide');
        $('#signupModal').modal('show');
    });

    // Xử lý form đăng ký
    $('#signup-form').on('submit', function (event) {
        event.preventDefault();

        const name = $('#signup-name').val().trim();
        const email = $('#signup-email').val().trim();
        const password = $('#signup-password').val().trim();

        if (!name || !email || !password) {
            alert('Vui lòng điền đầy đủ thông tin!');
            return;
        }

        $.ajax({
            url: 'http://localhost:8080/users',
            method: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({ name, email, password }),
            success: function (response) {
                alert('Đăng ký thành công! Vui lòng đăng nhập.');
                $('#signupModal').modal('hide');
                $('#loginModal').modal('show');
            },
            error: function (error) {
                alert('Đăng ký thất bại! Vui lòng thử lại.');
                console.error(error);
            }
        });
    });
});



