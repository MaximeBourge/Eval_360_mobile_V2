from flask import Flask, request, jsonify
from flask_cors import CORS  # Import CORS
import firebase_admin
from firebase_admin import credentials, firestore, auth
from google.api_core.exceptions import NotFound

app = Flask(__name__)
CORS(app, supports_credentials=True)  # Enable CORS and allow credentials

# Initialize Firebase
cred = credentials.Certificate('../../eval360-ff5bd-firebase-adminsdk-nmg6x-4b548fdf51.json')
firebase_admin.initialize_app(cred)

db = firestore.client()

# Endpoint for registering a teacher
@app.route('/register', methods=['POST'])
def register_teacher():
    data = request.json

    # Create a new user in Firebase Authentication
    user = auth.create_user(email=data['email'], password=data['password'])

    # Add the teacher's data to Firestore
    teacher_data = {
        'email': data['email'],
        'firstName': data['firstName'],
        'lastName': data['lastName'],
        'school': data['school'],
        'validated': False,
        'role': 'teacher'
    }

    db.collection('teachers').document(user.uid).set(teacher_data)

    return jsonify({'message': 'Teacher registered successfully'}), 200

# Endpoint for validating a teacher account by the admin
@app.route('/validate/<teacher_id>', methods=['PUT'])
def validate_teacher(teacher_id):
    # Update the 'validated' field in Firestore
    teacher_ref = db.collection('teachers').document(teacher_id)
    teacher_ref.update({'validated': True})

    return jsonify({'message': 'Teacher account validated successfully'}), 200

# Endpoint for fetching all teachers
@app.route('/teachers', methods=['GET'])
def get_all_teachers():
    teachers_ref = db.collection('teachers')
    teachers = [{'id': doc.id, **doc.to_dict()} for doc in teachers_ref.stream()]

    return jsonify(teachers), 200

# Endpoint for fetching an admin teacher
@app.route('/is-admin/<teacher_id>', methods=['GET'])
def is_admin(teacher_id):
    teacher_ref = db.collection('teachers').document(teacher_id)
    teacher = teacher_ref.get()

    if not teacher.exists:
        return jsonify({'error': 'Teacher not found'}), 404

    return jsonify({'isAdmin': teacher.to_dict().get('isAdmin', False)}), 200


# Endpoint for canceling a teacher registration
@app.route('/cancel/<teacher_id>', methods=['DELETE'])
def cancel_registration(teacher_id):
    try:
        teacher_ref = db.collection('teachers').document(teacher_id)
        teacher = teacher_ref.get()
        if not teacher.exists or teacher.to_dict()['validated']:
            return jsonify({'error': 'Teacher not found or already validated'}), 404

        auth.delete_user(teacher_id)
        teacher_ref.delete()

        return jsonify({'message': 'Teacher registration canceled successfully'}), 200
    except NotFound:
        return jsonify({'error': 'Teacher not found'}), 404

# Endpoint for deleting a teacher
@app.route('/delete/<teacher_id>', methods=['DELETE'])
def delete_teacher(teacher_id):
    try:
        teacher_ref = db.collection('teachers').document(teacher_id)
        teacher = teacher_ref.get()
        if not teacher.exists:
            return jsonify({'error': 'Teacher not found'}), 404

        auth.delete_user(teacher_id)
        teacher_ref.delete()

        return jsonify({'message': 'Teacher deleted successfully'}), 200
    except NotFound:
        return jsonify({'error': 'Teacher not found'}), 404

@app.route('/update-admin/<teacher_id>/<status>', methods=['PUT'])
def update_admin(teacher_id, status):
    teacher_ref = db.collection('teachers').document(teacher_id)
    teacher = teacher_ref.get()

    if not teacher.exists:
        return jsonify({'error': 'Teacher not found'}), 404

    # Convert the status string to a boolean
    is_admin = status.lower() == 'true'

    teacher_ref.update({'isAdmin': is_admin})
    return jsonify({'success': True}), 200

# Endpoint for fetching the projects of a specific teacher
@app.route('/projects/<teacher_id>', methods=['GET'])
def get_projects_by_teacher(teacher_id):
    teacher_ref = db.collection('teachers').document(teacher_id)
    teacher = teacher_ref.get()

    if not teacher.exists:
        return jsonify({'error': 'Teacher not found'}), 404

    teacher_data = teacher.to_dict()
    project_ids = teacher_data.get('projects', [])
    projects = []

    for project_id in project_ids:
        project_ref = db.collection('projects').document(project_id)
        project = project_ref.get()
        if project.exists:
            project_data = project.to_dict()
            project_data['teacherName'] = teacher_data['firstName'] + ' ' + teacher_data['lastName']
            projects.append({'id': project_id, **project_data})

    return jsonify(projects), 200

@app.route('/groups/<project_id>', methods=['GET'])
def get_groups_by_project(project_id):
    project_ref = db.collection('projects').document(project_id)
    project = project_ref.get()

    if not project.exists:
        return jsonify({'error': 'Project not found'}), 404

    group_ids = project.to_dict().get('groups', [])
    groups = []

    for group_id in group_ids:
        group_ref = db.collection('groups').document(group_id)
        group = group_ref.get()
        if group.exists:
            groups.append({'id': group_id, **group.to_dict()})

    return jsonify(groups), 200

@app.route('/students/<group_id>', methods=['GET'])
def get_students_by_group(group_id):
    group_ref = db.collection('groups').document(group_id)
    group = group_ref.get()

    if not group.exists:
        return jsonify({'error': 'Group not found'}), 404

    student_ids = group.to_dict().get('students', [])
    students = []

    for student_id in student_ids:
        student_ref = db.collection('students').document(student_id)
        student = student_ref.get()
        if student.exists:
            students.append({'id': student_id, **student.to_dict()})

    return jsonify(students), 200

@app.route('/is-validated/<teacher_id>', methods=['GET'])
def is_validated(teacher_id):
    teacher_ref = db.collection('teachers').document(teacher_id)
    teacher = teacher_ref.get()

    if not teacher.exists:
        return jsonify({'error': 'Teacher not found'}), 404

    return jsonify({'isValidated': teacher.to_dict().get('validated', False)}), 200





if __name__ == '__main__':
    app.run(port=5000, debug=True)





